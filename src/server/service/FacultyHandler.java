package server.service;

import server.domain.SystemFacade;

import java.util.UUID;

public class FacultyHandler {

    public String login(UUID sessionID, String email, String password) {
        return SystemFacade.getInstance().loginFaculty(sessionID, email, password);
    }

    public String getRegisteredStudents(int slotID) {
        return SystemFacade.getInstance().getRegisteredStudents(slotID);
    }

    public String getSlots(String courseID, int groupID) {
        return SystemFacade.getInstance().getSlots(courseID, groupID);
    }


}
