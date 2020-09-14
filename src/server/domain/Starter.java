package server.domain;

import server.communication.CommunicationController;
import server.dataAccess.Controller;

import java.io.IOException;

public class Starter {
    public static void main(String[] args) throws IOException {
        Controller.initiate();
        CommunicationController.start();
    }

}
