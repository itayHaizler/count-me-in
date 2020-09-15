package server.domain.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "lecture")
public class Lecture implements Serializable {

    @Id
    @Column(name = "studentID")
    private String studentID;

    @Id
    @Column(name = "slotID")
    private int slotID;

    public Lecture() {

    }

    public Lecture(String studentID, int slotID) {
        this.slotID = slotID;
        this.studentID = studentID;
    }

    public String getStudentID() { return studentID; }

    public int getSlotID() { return slotID; }
}
