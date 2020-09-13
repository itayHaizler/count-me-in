package server.domain.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;

import java.util.Date;

public class Slot {

    private Date date;
    private String courseID;
    private int groupID;
    private int slotID;

    public Slot(){}

    public int getSlotID() {
        return slotID;
    }

}
