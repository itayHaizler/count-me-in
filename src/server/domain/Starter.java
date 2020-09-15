package server.domain;

import server.communication.CommunicationController;
import server.dataAccess.Controller;
import server.service.SystemInitHandler;

import java.io.IOException;
import java.text.ParseException;

public class Starter {
    public static void main(String[] args) throws IOException, ParseException {
        Controller.initiate();
        (new SystemInitHandler()).initSystem("./configurations/script.ini");

        CommunicationController.start();

//        Slot s = new Slot("22", 22, 1, 3, 14, 1, 2);
//        Controller.create(s);
//
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = df.parse("2020-09-13 12:00:00");
//        SlotDates sd = new SlotDates(1, date, 1);
//        Controller.create(sd);
//
//        Student s1 = new Student("1", "toya", 100);
//        Student s2 = new Student("2", "lucy", 100);
//        Student s3 = new Student("3", "cooper", 100);
//
//        Controller.create(s1);
//        Controller.create(s2);
//        Controller.create(s3);
//
//        Bid b1 = new Bid(s.getSlotID(), s1.getID(), 20);
//        Bid b2 = new Bid(s.getSlotID(), s2.getID(), 30);
//        Bid b3 = new Bid(s.getSlotID(), s3.getID(), 40);
//
//        Controller.create(b1);
//        Controller.create(b2);
//        Controller.create(b3);

        SystemFacade.getInstance().calculateBiding();
    }

}
