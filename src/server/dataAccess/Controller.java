package server.dataAccess;

import server.domain.models.Assignings;
import server.domain.models.Slot;

import java.util.List;

public class Controller {

    public static int getStudentPoints(String studentID) {
        return 0;
    }

    public static void setStudentPoints(String studentID, int newPoints) {
    }

    public static List<Slot> getSlotsForFacultyMember(String courseID, int groupID) {
        return null;
    }

    public static List<Assignings> getAssigningsOfStudent(String studentID) {
        return null;
    }

    public static List<Slot> getSlotsOfStudent(String studentID, boolean forBiding) {
        // TODO: filter dates- if forBiding: 13-9-2020 - 25-9-2020
        // TODO:               for schedule: 13-9-2020 - 25-9-2020
        return null;
    }

    public static List<Assignings> getAssigningsOfSlot(int slotID) {
        return null;
    }

    public static void setStudentBid(String studentID, int slotID, int percentage) {
    }
}
