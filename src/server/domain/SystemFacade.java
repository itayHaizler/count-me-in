package server.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import server.dataAccess.Controller;
import org.json.simple.JSONObject;
import server.domain.models.Assignings;
import server.domain.models.Slot;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
            Gson gson = new GsonBuilder().create();
            JSONParser parser = new JSONParser();
            JSONObject slotJson = (JSONObject) parser.parse(gson.toJson(slot));
            jsonArray.add(slotJson);
        }
        return jsonArray.toString();
    }

    public String loginStudent(UUID sessionID, String email, String password) {
        Session se = active_sessions.get(sessionID);
        if(se == null)
            throw new IllegalArgumentException("Invalid Session ID");
        // TODO: how to validate user???????????
        if (true) {
            this.isAdminMode = false;
            String studentID = Controller.getStudentID(email, password);
            se.setStudentID(studentID);
            return "Valid Student";
        }
        return "Invalid Student";
    }

    public String loginFaculty(UUID sessionID, String email, String password) {
        // TODO: need to secure this shittttttttttt
        if (email.equals("Admin@post.bgu.ac.il") && password.equals("1234")) {
            this.isAdminMode = true;
            return createJSONMsg("SUCCESS", "Valid Admin");
        }
        return "Invalid Admin";
    }

    public String getStudentSchedule(UUID sessionID) throws ParseException {
        Session se = active_sessions.get(sessionID);
        if(se == null)
            throw new IllegalArgumentException("Invalid Session ID");
        String studentID = se.getStudentID();
        List<Slot> allSlots = Controller.getSlotsOfStudent(studentID, false);
        List<Assignings> assignings = Controller.getAssigningsOfStudent(studentID);
        HashMap<Integer, Boolean> greenSlots = new HashMap<Integer, Boolean>();
        for (Assignings as : assignings) {
            greenSlots.put(as.getSlotID(), true);
        }

        JSONArray allSlotsJson = new JSONArray();

        // [ {slot id: , date, courseId, groupId, isApproved}]
        JSONObject slotJson = new JSONObject();
        for (Slot slot : allSlots) {
            Gson gson = new GsonBuilder().create();
            JSONParser parser = new JSONParser();
            slotJson = (JSONObject) parser.parse(gson.toJson(slot));
            slotJson.put("isApproved", greenSlots.containsKey(slot.getSlotID()));
            allSlotsJson.add(slotJson);
        }
        return allSlotsJson.toString();
    }

    public String getStudentScheduleForBiding(UUID sessionID) throws ParseException{
        Session se = active_sessions.get(sessionID);
        if(se == null)
            throw new IllegalArgumentException("Invalid Session ID");
        String studentID = se.getStudentID();
        List<Slot> allSlots = Controller.getSlotsOfStudent(studentID, true);
        JSONArray allSlotsJson = new JSONArray();

        // [ {slot id: , date, courseId, groupId} ]
        JSONObject slotJson = new JSONObject();
        for (Slot slot : allSlots) {
            Gson gson = new GsonBuilder().create();
            JSONParser parser = new JSONParser();
            slotJson = (JSONObject) parser.parse(gson.toJson(slot));
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

    public void calculateBiding() {
        // get all bids
        // calculate points of every student per slot
        // choose top capacity students with highest points
        // update points for winning students
    }

    public String createJSONMsg(String type, String content) {
        JSONObject response = new JSONObject();
        response.put(type, content);
        return response.toJSONString();
    }

    public void updatePointsForStudent(String studentID, int newPoints) {
        Controller.setStudentPoints(studentID, newPoints);
    }
}
