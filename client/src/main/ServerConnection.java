package main;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

public class ServerConnection {
    private String sessionCode;
    private String url;
    private long verificationKey;

    public ServerConnection(String url, String sessionCode){
        this.url = url;
        this.sessionCode = sessionCode;
        this.verificationKey = ThreadLocalRandom.current().nextLong();
    }

    /**
     * Connect to a session - perform handshake and request work
     */
    public void createSessionConnection(){
        if(isSuccessfulHandshake()){
            //TODO: implement requesting work
        }
    }

    /**
     * Perform handshake operations and determine validity
     * @return validity of the handhshake operation
     */
    private boolean isSuccessfulHandshake(){
        String handshakeArgs = getHandshakeArgumentObject().toString();
        HttpURLConnection connection = createConnection(handshakeArgs.length());
        sendRequest(connection, handshakeArgs);
        JSONObject handshakeResponse = getResponse(connection);
        return handshakeResponse.getString("response").equals(Long.toString(this.verificationKey));
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
     * Create HTTP connection to the specified server url
     * @param contentLength length of the content to be sent to the server
     * @return an http connection object
     */
    private HttpURLConnection createConnection(int contentLength){
        HttpURLConnection connection = null;
        try {
            //Open connection to specified url
            URL url = new URL(this.url);
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
