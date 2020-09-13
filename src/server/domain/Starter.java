package server.domain;

import server.communication.CommunicationController;

import java.io.IOException;

public class Starter {
    public static void main(String[] args) throws IOException {
//        PersistenceController.initiate(true);
//        (new SystemInitHandler()).initSystem("./configurations/init.ini");
        CommunicationController.start();
    }

}
