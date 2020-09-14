package server.domain.models;

public class Bid {
    private int slotID;
    private String studentID;
    private int percentage;

    public Bid(){}

    public int getPercentage() {
        return percentage;
    }
}
