package main;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

public class ServerConnection {
    private String sessionCode;
    private String url;
    private long verificationKey;

    public ServerConnection(String url, String sessionCode){
        this.url = "http://" + url + ":6699";
        this.sessionCode = sessionCode;
        this.verificationKey = ThreadLocalRandom.current().nextLong();
    }

    /**
     * Perform handshake operations and determine validity
     * @return validity of the handshake operation
     */
    public boolean isSuccessfulHandshake(){
        //Get argument object to send with handshake request
        String handshakeArgs = getHandshakeArgumentObject().toString();
        //Create connection object from authenticate user path with argument object string length
        HttpURLConnection connection = createConnection("/authenticate_client", handshakeArgs.length());
        //Send the request to the server
        sendRequest(connection, handshakeArgs);
        //Get the response object from the server
        JSONObject handshakeResponse = getResponse(connection);
        //Return validity of the handshake - determined from response object
        return handshakeResponse.getString("response").equals(Long.toString(this.verificationKey));
    }

    /**
     * Get a specified number of files from the server distribution module
     * @param coreCount amount of files to request - the number of files that can be parsed at one time
     * @return a string array containing the urls of the requested files
     */
    public String[] getWorkFileUrls(int coreCount){
        //Get argument object to send with request work args
        String requestWorkArgs = getRequestWorkArgumentObject(coreCount).toString();
        //Create connection object from request work path with argument object string length
        HttpURLConnection conn = createConnection("/request_task", requestWorkArgs.length());
        //Send the request to the server
        sendRequest(conn, requestWorkArgs);
        //Get the response object from the server
        JSONObject requestWorkResponse = getResponse(conn);
        //Get the file json array - this contains the requested files on which to do work
        JSONArray requestedWorkArray = requestWorkResponse.getJSONArray("files");
        //Get the requested file urls and move into string array
        String[] workFileUrls = new String[requestedWorkArray.length()];
        for(int i = 0; i < requestedWorkArray.length(); i++) {
            workFileUrls[i] = requestedWorkArray.getString(i);
        }
        return workFileUrls;
    }

    /**
     * Get the arguments to send with the handshake request
     * @return JSON object containing the handshake request arguments
     */
    private JSONObject getHandshakeArgumentObject(){
        //Create handshake arguments object
        JSONObject handshakeArgs = new JSONObject();
        //Append handshake argument properties into object
        handshakeArgs.put("session_code", this.sessionCode);
        handshakeArgs.put("verification_key", this.verificationKey);
        return handshakeArgs;
    }

    /**
     * Get the arugmetns to send with the request work request
     * @param coreCount number of work to request in this request
     * @return JSON object containing the request work request arguments
     */
    private JSONObject getRequestWorkArgumentObject(int coreCount){
        //Create request work arguments object
        JSONObject requestWorkArgs = new JSONObject();
        //Append the request work argument properties to the object
        requestWorkArgs.put("verification_key", this.verificationKey);
        requestWorkArgs.put("request_count", coreCount);
        return requestWorkArgs;
    }

    /**
     * Create HTTP connection to the specified server url
     * @param contentLength length of the content to be sent to the server
     * @return an http connection object
     */
    private HttpURLConnection createConnection(String urlPath, int contentLength){
        HttpURLConnection connection = null;
        try {
            //Open connection to specified url
            URL url = new URL(this.url + urlPath);
            connection = (HttpURLConnection) url.openConnection();
            //Define the arguments of the connection header
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", Integer.toString(contentLength));
            connection.setRequestProperty("Content-Language", "en-US");
            //Define the connection object properties
            connection.setUseCaches(false);
            connection.setDoOutput(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return connection;
    }

    /**
     * Send a request to the server via HTTP connection
     * @param connection Connection object to send the request through
     * @param requestString Text to send via request
     */
    private void sendRequest(HttpURLConnection connection, String requestString){
        try{
            //Create output stream for request body
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            //Write the request via connection
            dataOutputStream.writeBytes(requestString);
            dataOutputStream.close();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    /**
     * Get the response from an HTTP request
     * @param connection HTTP connection to get the response from
     * @return JSON object containing the response
     */
    private JSONObject getResponse(HttpURLConnection connection){
        try{
            //Get the input stream from the connection
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder responseString = new StringBuilder();
            String responseLine;
            //Read the input stream to the response string
            while((responseLine = bufferedReader.readLine()) != null){
                responseString.append(responseLine);
            }
            bufferedReader.close();
            //Convert the response string to the JSON object
            return new JSONObject(responseString.toString());
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
        return null;
    }
}
