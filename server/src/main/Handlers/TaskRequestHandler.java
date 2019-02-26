package main.Handlers;

import com.sun.net.httpserver.HttpHandler;
import main.Managers.FDADataManager;
import main.Managers.SessionInstanceManager;
import org.json.JSONArray;
import org.json.JSONObject;


public class TaskRequestHandler extends HandlerPrototype implements HttpHandler {
    public TaskRequestHandler(){
        requiredKeys = new String[] { "verification_key", "request_count" };
    }
    @Override
    protected void fulfillRequest(JSONObject requestParams){
        long verificationKey = requestParams.getLong("verification_key");
        //Validate that the requesting client is attached to the session
        if(SessionInstanceManager.isAttachedClient(verificationKey)){
            int requestCount = requestParams.getInt("request_count");
            JSONArray taskResponseArray = new JSONArray();
            //Return the requested amount of files
            for(int i = 0; i < requestCount; i++) {
                try{
                    //Get the next file from the file queue
                    taskResponseArray.put(FDADataManager.getNextFile());
                } catch (InterruptedException iEx) {
                    iEx.printStackTrace();
                }
            }
            //Return the requested files as a json array string
            this.response = new JSONObject().put("files", taskResponseArray).toString();
        } else {
            this.response = "invalid client - not attached to session";
        }
    }
}
