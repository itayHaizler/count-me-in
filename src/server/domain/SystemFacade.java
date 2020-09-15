package server.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.bytebuddy.implementation.bytecode.Throw;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import server.dataAccess.Controller;
import org.json.simple.JSONObject;
import server.domain.models.*;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;


public class SystemFacade {

    private boolean isAdminMode;
    private ConcurrentHashMap<UUID,Session> active_sessions;

    private static SystemFacade ourInstance = new SystemFacade();

    public static SystemFacade getInstance() {
        return ourInstance;
    }


    public SystemFacade(){
        this.active_sessions = new ConcurrentHashMap<>();
    }

    //--------------------------------------handle sessions----------------------------------------
    public UUID createNewSession(){
        Session newSession = new Session();
        UUID sessionID = newSession.getSession_id();
        active_sessions.put(sessionID, newSession);
        return sessionID;
    }

    public void closeSession(UUID session_id) {
        if(!active_sessions.containsKey(session_id))
            throw new IllegalArgumentException("Invalid Session ID");
        active_sessions.remove(session_id);
    }
    //----------------------------------------------------------------------------------------------


    public String getStudentPoints(UUID sessionId) {
        Session se = active_sessions.get(sessionId);
        if(se == null)
            throw new IllegalArgumentException("Invalid Session ID");
        return createJSONMsg("SUCCESS", Integer.toString(Controller.getStudentPoints(se.getStudentID())));
    }

    private void updatePoints(String studentID, int pointsDeducted) {
        int newPoints = Controller.getStudentPoints(studentID) - pointsDeducted;
        Controller.setStudentPoints(studentID, newPoints);
    }

    public String getRegisteredStudents(int slotID) {
        if (!isAdminMode)
            return createJSONMsg("ERROR", "Faculty access only");
        else {
            List<Assignings> assignings = Controller.getAssigningsOfSlot(slotID);
            JSONArray assigningsJson = new JSONArray();
            JSONObject assJson;
            for (Assignings asg : assignings) {
                assJson = new JSONObject();
                assJson.put("name", asg.getStudent().getName());
                assJson.put("studentID", asg.getStudentID());
                assigningsJson.add(assJson);
            }
            return assigningsJson.toJSONString();
        }
    }

    public String getSlots(String courseID, int groupID) throws ParseException {
        List<Slot> slots = Controller.getSlotsForFacultyMember(courseID, groupID);
        JSONArray jsonArray = new JSONArray();
        for (Slot slot : slots) {
            List<SlotDates> sds = Controller.getAllSlotDatesBySlotID(slot.getSlotID());
            for(SlotDates sd: sds) {
                JSONObject jo = new JSONObject();
                jo.put("slotDateID", sd.getSlotDateID());
                jo.put("date", sd.getDate().getTime());
                jsonArray.add(jo);
            }
        }
        return jsonArray.toString();
    }

    public String loginStudent(UUID sessionID, String email, String password, String studentID) {
        Session se = active_sessions.get(sessionID);
        if(se == null)
            throw new IllegalArgumentException("Invalid Session ID");
        if(!Controller.validPassword(email, password))
            throw new IllegalArgumentException("invalid password");

        if (true) {
            this.isAdminMode = false;
            se.setStudentID(studentID);
            return "Valid Student";
        }
        return "Invalid Student";
    }

    public String loginFaculty(UUID sessionID, String email, String password) {
        Session se = active_sessions.get(sessionID);
        if(se == null)
            throw new IllegalArgumentException("Invalid Session ID");
        if (email.equals("Admin@post.bgu.ac.il") && password.equals("1234")) {
            this.isAdminMode = true;
            se.setAdminMode(true);
            return createJSONMsg("SUCCESS", "Valid Admin");
        }
        return "Invalid Admin";
    }

    public String getStudentSchedule(UUID sessionID) throws ParseException {
        Session se = active_sessions.get(sessionID);
        if(se == null)
            throw new IllegalArgumentException("Invalid Session ID");
        String studentID = se.getStudentID();
        List<SlotDates> allSlotsDates = Controller.getSlotsDatesOfStudent(studentID);
        List<Assignings> assignings = Controller.getAssigningsOfStudent(studentID);
        HashMap<Date, Boolean> greenSlots = new HashMap<>();
        for (Assignings as : assignings) {
            greenSlots.put(as.getDate(), true);
        }

        JSONArray allSlotsJson = new JSONArray();

        // [ {slot id: day, hour, date, courseId, groupId, isApproved}]
        JSONObject slotJson = new JSONObject();
        for (SlotDates slotDate : allSlotsDates) {
            Slot slot = Controller.getSlotOfSlotDate(slotDate);
            Gson gson = new GsonBuilder().create();
            JSONParser parser = new JSONParser();
            slotJson = (JSONObject) parser.parse(gson.toJson(slot));
            slotJson.put("date", slotDate.getDate().getTime());
            slotJson.put("isApproved", greenSlots.containsKey(slotDate.getDate()));
            allSlotsJson.add(slotJson);
        }
        return allSlotsJson.toString();
    }

    public String getStudentScheduleForBiding(UUID sessionID) throws ParseException {
        Session se = active_sessions.get(sessionID);
        if(se == null)
            throw new IllegalArgumentException("Invalid Session ID");
        String studentID = se.getStudentID();
        List<Slot> allSlots = Controller.getAllSlotsOfStudent(studentID);
        JSONArray allSlotsJson = new JSONArray();

        // [ {slot id: hour, day, courseId, groupId, duration, bidingPercentage} ]
        JSONObject slotJson = new JSONObject();
        Bid currBid;
        for (Slot slot : allSlots) {
            currBid = Controller.getBidForSlotOfStudent(slot.getSlotID(),studentID);
            Gson gson = new GsonBuilder().create();
            JSONParser parser = new JSONParser();
            slotJson = (JSONObject) parser.parse(gson.toJson(slot));
            slotJson.put("bidingPercentage", currBid.getPercentage());
            allSlotsJson.add(slotJson);
        }
        return allSlotsJson.toString();
    }

    public void setBid(UUID sessionID, int slotID, int percentage) {
        Session se = active_sessions.get(sessionID);
        if(se == null)
            throw new IllegalArgumentException("Invalid Session ID");
        Controller.setStudentBid(se.getStudentID(), slotID, percentage);
    }

    public void calculateBiding() throws java.text.ParseException {
        // get all slots id
        List<SlotDates> slotDates = Controller.getAllSlotDatesRange();
        HashMap<String, Integer> studWinBids;
        // studentIDWinner, Points
        for (SlotDates sl: slotDates) {
            Slot currSlot = Controller.getSlotByID(sl.getSlotID());
            int capacity = currSlot.getCapacity();
            studWinBids = Controller.getAssignedStudentsForSlot(sl.getSlotID(), capacity);

            Assignings as;
            Student stud;
            for(String studentID: studWinBids.keySet()){
                stud = Controller.getStudentByID(studentID);
                as = new Assignings(sl.getSlotDateID(), stud, sl.getDate());

                //save new assigning to DB
                Controller.create(as);

                //update student points
                //stud.setTotalPoints(stud.getTotalPoints() - studWinBids.get(studentID));
                updatePointsForStudent(stud.getID(),stud.getTotalPoints() - (studWinBids.get(studentID)/100)    );
            }
        }
    }

    public void updatePointsForStudent(String studentID, int newPoints) {
        Controller.setStudentPoints(studentID, newPoints);
    }

    public String createJSONMsg(String type, String content) {
        JSONObject response = new JSONObject();
        response.put(type, content);
        return response.toJSONString();
    }

}
