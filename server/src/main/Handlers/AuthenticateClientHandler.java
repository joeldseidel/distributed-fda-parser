package main.Handlers;

import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

public class AuthenticateClientHandler extends HandlerPrototype implements HttpHandler {
    public AuthenticateClientHandler(){
        requiredKeys = new String[] { "session_id", "verification_key" };
    }
    @Override
    protected void fulfillRequest(JSONObject requestParams){

    }
}
