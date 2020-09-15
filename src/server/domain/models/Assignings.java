package server.domain.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "assignings")
public class Assignings implements Serializable {

    @Id
    @Column(name="slotDateID")
    private int slotDateID;

    @Id
    @Column(name = "studentID")
    private String studentID;

    @Column(name = "date")
    private Date date;

    @Transient
    private Student student;

    public Assignings(){}

    public Assignings(int slotDateID, Student student, Date date){
        this.student = student;
        this.studentID = student.getID();
        this.slotDateID = slotDateID;
        this.date = date;
    }

    public int getSlotDateID() {
        return slotDateID;
    }

    public String getStudentID() {
        return studentID;
    }

    public Date getDate() {
        return this.date;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student s){
        this.student = s;
    }
}
