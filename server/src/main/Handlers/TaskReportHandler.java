package main.Handlers;

import com.sun.net.httpserver.HttpHandler;
import main.Managers.ProcessedDataManager;
import main.Managers.SessionInstanceManager;
import org.json.JSONArray;
import org.json.JSONObject;

public class TaskReportHandler extends HandlerPrototype implements HttpHandler {
    public TaskReportHandler(){
        requiredKeys = new String[] { "verification_key", "data", "seq", "complete" };
        handlerName = "Task Report Handler";
    }
    @Override
    protected void fulfillRequest(JSONObject requestParams){
        long verificationKey = requestParams.getLong("verification_key");
        //Determine if the sending client is attached to this session
        if(!SessionInstanceManager.isAttachedClient(verificationKey)){
            //Client is not attached to this session
            this.response = "not attached client";
        } else {
            String processedData = requestParams.getString("data");
            try {
                //Add the processed data to the commit queue
                ProcessedDataManager.addProcessedData(processedData);
            } catch(InterruptedException iEx) {
                iEx.printStackTrace();
            }
            int seq = requestParams.getInt("seq");
            boolean isComplete = requestParams.getBoolean("complete");
            //Respond with an ack number if not complete or thank the client :)
            if(isComplete){
                //Processing of this file is complete, thank the client
                this.response = "thank you";
            } else {
                //This is just a portion of the file. Tell the client which seq to send next
                this.response = Integer.toString(seq + 1);
            }
        }
    }
}
