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
        SystemFacade.getInstance().calculateBiding();
    }

}
