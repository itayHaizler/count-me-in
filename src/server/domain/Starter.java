package server.domain;

import server.communication.CommunicationController;
import server.dataAccess.Controller;
import server.domain.models.Assignings;
import server.domain.models.Student;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Starter {
    public static void main(String[] args) throws IOException {
        Controller.initiate();
        CommunicationController.start();


        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = df.parse("2020-09-12 00:00:00");
            Date d2 = df.parse("2020-09-15 00:00:00");
            Student s = new Student("222", "toya", 100);
            Assignings a1 = new Assignings(1, s, d1);
            Assignings a2 = new Assignings(2, s, d2);
            Controller.create(a1);
            Controller.create(a2);

            List<Assignings> as = Controller.getAssigningsOfStudent("222");
            int length = as.size();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
