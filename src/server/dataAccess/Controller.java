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

    public static List<Slot> getSlotsOfStudent(String studentID) {
        return null;
    }

    public static List<Assignings> getAssigningsOfSlot(int slotID) {
        return null;
    }

    public static void setStudentBid(String studentID, int slotID, int percentage) {
    }

    public static String getStudentID(String email, String password){
        return null;
    }
}
