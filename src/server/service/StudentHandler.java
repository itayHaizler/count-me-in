package server.service;

import server.dataAccess.Controller;
import server.domain.SystemFacade;

import java.util.UUID;

public class StudentHandler {

    public String login(UUID sessionID, String email, String password) {
        return SystemFacade.getInstance().loginStudent(sessionID, email, password);
    }

    public String getStudentPoints(UUID sessionID) {
        return SystemFacade.getInstance().getStudentPoints(sessionID);
    }

    public void updatePercentage(UUID sessionID, int slotID, int percentage) {
    }

    public String getSchedule(UUID sessionID) {
        return SystemFacade.getInstance().getStudentSchedule(sessionID);
    }

    public String getScheduleBiding(UUID sessionID) {
        return SystemFacade.getInstance().getStudentScheduleForBiding(sessionID);
    }
}
