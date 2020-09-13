package server.domain;

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
            createJSONMsg("ERROR","Faculty access only");
        else {

        }
    }

    public String getSlots(String courseID, int groupID) {
        return Controller.getSlotsForFacultyMember(courseID, groupID);
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
            return createJSONMsg("SUCCESS","Valid Admin");
        }
        return "Invalid Admin";
    }


    public String getStudentSchedule(UUID sessionID) {
        // TODO: get studentId with session
        String studentID = "";
        List<Slot> allSlots = Controller.getSlotsOfStudent(studentID);
        List<Assignings> assignings = Controller.getAssigningsOfStudent(studentID);
        HashMap<Integer, Boolean> isGreenSlot = new HashMap<Integer, Boolean>();
        for (Assignings as: assignings) {
            isGreenSlot.put(as.getSlotID(), true);
        }

        for (Slot slot: allSlots) {
            // TODO: create JSON array of slots in schedule
            if (slot.getDay() > 7) {
                // week 2 in JSON
            }
            else {
                // week 1 in JSON
            }
            if (isGreenSlot.containsKey(slot.getSlotID())) {
                // green slot in JSON
            }
        }
        return "";
    }
    public String getStudentScheduleForBiding(UUID sessionID) {
        // TODO: get studentId with session
        String studentID = "";
        List<Slot> allSlots = Controller.getSlotsOfStudent(studentID);
        for (Slot slot: allSlots) {
            // TODO: create JSON array of slots in schedule
            if (slot.getDay() <= 7) {
                // add to JSON
            }
            else {
                // don't add to JSON
            }
        }
        return "";
    }

    public String createJSONMsg(String type, String content) {
        JSONObject response = new JSONObject();
        response.put(type, content);
        return response.toJSONString();
    }
}
