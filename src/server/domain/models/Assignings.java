package server.domain.models;

import java.util.Date;

public class Assignings {

    private int slotID;
    private String studentID;
    private Student student;
    private Date date;

    public int getSlotID() {
        return slotID;
    }

    public String getStudentID() {
        return studentID;
    }

    public Student getStudent() {
        return student;
    }

}
