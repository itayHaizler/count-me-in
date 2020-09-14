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

    }

}
