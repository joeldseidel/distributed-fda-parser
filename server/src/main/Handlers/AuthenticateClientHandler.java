package main.Handlers;

import com.sun.net.httpserver.HttpHandler;
import main.Managers.SessionInstanceManager;
import org.json.JSONObject;

public class AuthenticateClientHandler extends HandlerPrototype implements HttpHandler {
    public AuthenticateClientHandler(){
        requiredKeys = new String[] { "session_code", "verification_key" };
        handlerName = "Authenticate User Handler";
    }
    @Override
    protected void fulfillRequest(JSONObject requestParams){
        String sessionCode = requestParams.getString("session_code");
        //Verify the session code
        if(SessionInstanceManager.isValidSessionCode(sessionCode)){
            //Session code was valid ~ attach the client as an authenticated worked
            long verificationKey = requestParams.getLong("verification_key");
            //Attach the client to as an authenticated worker
            SessionInstanceManager.attachClient(verificationKey);
            //Debug output statement
            System.out.println("New worker client attached");
            //Return the validated verification as a the response
            this.response = Long.toString(verificationKey);
        } else {
            //Session code was invalid
            this.response = "invalid client handshake";
        }
    }
}
