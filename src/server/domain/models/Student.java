package server.domain.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "student")
public class Student implements Serializable {

    @Id
    @Column(name = "studentID")
    private String studentID;

    @Column(name = "totalPoints")
    private int totalPoints;

    @Column(name = "name")
    private String name;

    public Student(){}

    public Student(String studentID, String name, int totalPoints){
        this.studentID = studentID;
        this.name = name;
        this.totalPoints = totalPoints;
    }

    public String getName() {
        return name;
    }

    public String getID() { return studentID; }

    public int getTotalPoints() { return this.totalPoints; }

    public void setTotalPoints(int newTotalPoints) { this.totalPoints = newTotalPoints; }
}