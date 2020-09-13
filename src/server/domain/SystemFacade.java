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

public class SystemFacade {

    private boolean isAdminMode;

    private static SystemFacade ourInstance = new SystemFacade();

    public static SystemFacade getInstance() {
        return ourInstance;
    }

    public String getStudentPoints(UUID sessionId) {

        // TODO: check session id and get student id
        String studentID = "111";
        return createJSONMsg("SUCCESS", Integer.toString(Controller.getStudentPoints(studentID)));
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
            for (Assignings asg : assignings) {
                // TODO: create JSON with:
                asg.getStudent().getName();
                asg.getStudentID();
            }
        }
        return "";
    }

    public String getSlots(String courseID, int groupID) throws ParseException {
        List<Slot> slots = Controller.getSlotsForFacultyMember(courseID, groupID);
        JSONArray jsonArray = new JSONArray();
        for (Slot slot : slots) {
            JSONObject slotJson = new JSONObject();
            Gson gson = new GsonBuilder().create();
            JSONParser parser = new JSONParser();
            slotJson = (JSONObject) parser.parse(gson.toJson(slot));
            jsonArray.add(slotJson);
        }
        return jsonArray.toString();
    }

    public String loginStudent(UUID sessionID, String email, String password) {
        // TODO: how to validate user???????????
        if (true) {
            this.isAdminMode = false;
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
        // TODO: get studentId with session
        String studentID = "";
        List<Slot> allSlots = Controller.getSlotsOfStudent(studentID);
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

    public String getStudentScheduleForBiding(UUID sessionID) throws ParseException {
        // TODO: get studentId with session
        String studentID = "";
        List<Slot> allSlots = Controller.getSlotsOfStudent(studentID);
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
        // TODO: get student id from session id
        String studentID = "000";
        Controller.setStudentBid(studentID, slotID, percentage);
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
