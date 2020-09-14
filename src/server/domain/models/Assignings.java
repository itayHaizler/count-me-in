package server.domain.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "assignings")
public class Assignings implements Serializable {

    @Id
    @Column(name="slotID")
    private int slotID;

    @Id
    @Column(name = "studentID")
    private String studentID;

    @Transient
    private Student student;

    @Id
    @Column(name = "date")
    private Date date;

    public Assignings(){}

    public Assignings(int slotID, Student student, Date date){
        this.student = student;
        this.studentID = student.getID();
        this.slotID = slotID;
        this.date = date;
    }

    public int getSlotID() {
        return slotID;
    }

    public String getStudentID() {
        return studentID;
    }

    public Student getStudent() {
        return student;
    }

    public Date getDate() {
        return date;
    }

}
