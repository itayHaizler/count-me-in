package server.domain.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "schedule")
public class Schedule implements Serializable {

    @Id
    @Column(name = "studentID")
    private String studentID;

    @Id
    @Column(name = "slotID")
    private int slotID;

    public Schedule() {

    }

    public Schedule(String studentID, int slotID) {
        this.slotID = slotID;
        this.studentID = studentID;
    }

    public String getStudentID() { return studentID; }

    public int getSlotID() { return slotID; }
}
