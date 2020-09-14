package server.domain;

import server.domain.models.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Session {


    private UUID session_id;
    private String student_id;
    private boolean adminMode;


    public Session() {
        session_id = UUID.randomUUID();
        adminMode = false;
    }

    public String getStudentID() {
        return student_id;
    }

    public void setStudentID(String studentID) {
        this.student_id = studentID;
    }

    public boolean isAdminMode() {
        return adminMode;
    }

    public void setAdminMode(boolean adminMode) {
        this.adminMode = adminMode;
    }

    public UUID getSession_id() {
        return session_id;
    }




}
