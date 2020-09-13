package server.service;

import server.domain.SystemFacade;

import java.util.UUID;

public class SessionHandler {
    public String openNewSession(){
        return SystemFacade.getInstance().createNewSession().toString();
    }

    public String closeSession(UUID session_id){
        try {
            SystemFacade.getInstance().closeSession(session_id);
            return "Session Ended";
        }
        catch (Exception e){
            return  e.getMessage();
        }
    }
}
