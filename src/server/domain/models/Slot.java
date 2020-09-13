package server.domain.models;

public class Slot {
    int day;
    int hour;
    String courseID;
    int groupID;
    int duration;
    int slotID;

    public Slot(){}

    public int getSlotID() {
        return slotID;
    }

    public int getDay() {
        return this.day;
    }
}
