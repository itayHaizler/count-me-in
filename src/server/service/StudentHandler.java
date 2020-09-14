package server.service;

import org.json.simple.parser.ParseException;
import server.dataAccess.Controller;
import server.domain.SystemFacade;

import java.util.UUID;

public class StudentHandler {

    public String login(UUID sessionID, String email, String password, String studentID) {
        return SystemFacade.getInstance().loginStudent(sessionID, email, password, studentID);
    }

    public String getStudentPoints(UUID sessionID) {
        return SystemFacade.getInstance().getStudentPoints(sessionID);
    }

    public void updatePercentage(UUID sessionID, int slotID, int percentage) {
        SystemFacade.getInstance().setBid(sessionID, slotID, percentage);
    }

    public String getSchedule(UUID sessionID) throws ParseException {
        return SystemFacade.getInstance().getStudentSchedule(sessionID);
    }

    public String getScheduleBiding(UUID sessionID) throws ParseException {
        return SystemFacade.getInstance().getStudentScheduleForBiding(sessionID);
    }

}
