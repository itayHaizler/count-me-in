package server.domain.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "bid")
public class Bid implements Serializable {

    @Id
    @Column(name="slotID")
    private int slotID;

    @Id
    @Column(name="studentID")
    private String studentID;

    @Column(name="percentage")
    private int percentage;

    public Bid(){}

    public Bid(int slotID, String studentID, int percentage){
        this.slotID = slotID;
        this.studentID = studentID;
        this.percentage = percentage;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int newPercentage) {
        this.percentage = newPercentage;
    }

    public int getSlotID() { return slotID; }

    public String getStudentID() { return studentID; }

}
