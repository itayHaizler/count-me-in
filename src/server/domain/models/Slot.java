package server.domain.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "slot")
public class Slot implements Serializable {

    @Id
    @Column(name = "slotID", unique = true)
    @GeneratedValue
    private int slotID;

    @Column(name = "courseID")
    private String courseID;

    @Column(name = "groupID")
    private int groupID;

    @Column(name = "day")
    private int day;

    @Column(name = "hour")
    private int hour;

    @Column(name = "duration")
    private int duration;

    public Slot(){}

    public Slot(String courseID, int groupID, int slotID, int day, int hour, int duration){
        this.courseID = courseID;
        this.groupID = groupID;
        this.slotID = slotID;
        this.day = day;
        this.hour = hour;
        this.duration = duration;
    }

    public int getSlotID() {
        return slotID;
    }

    public int getGroupID() {
        return groupID;
    }

    public int getDay() {
        return day;
    }

    public int getHour() { return hour;}

    public int getDuration() {
        return duration;
    }
}
