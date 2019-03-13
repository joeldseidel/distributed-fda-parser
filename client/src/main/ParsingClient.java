package main;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ParsingClient {
    public static void main(String args[]){
        //Attempt to connect with the server
        ServerConnection conn;
        do {
            conn = connectToSession();
        } while(conn == null);
        initWorker(conn);
    }

    /**
     * Establish connection to the server and verify handshake
     * @return Server connection object
     */
    private static ServerConnection connectToSession(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //Print the console header
        System.out.println("Maverick Software Systems FDA Data Parsing Client");
        System.out.println("\n\n\n");
        //Get the server url user input
        System.out.println("Enter server URL: ");
        String serverUrl;
        try{
            serverUrl = br.readLine();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            return null;
        }
        //Get the session code user input value
        System.out.println("Enter the session code: ");
        String sessionCode;
        try{
            sessionCode = br.readLine();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            return null;
        }
        //Instantiate server connection object with provided url and session code
        ServerConnection conn = new ServerConnection(serverUrl, sessionCode);
        //Attempt handshake with server
        if(conn.isSuccessfulHandshake()){
            return conn;
        } else {
            return null;
        }
    }

    private static void initWorker(ServerConnection conn) {
        //Get available processors - benchmark for how many tasks we can do at one time
        int availableCores = Runtime.getRuntime().availableProcessors();
        //Get the array of files from the server with the available number of threads
        String[] workFilesUrls = conn.getWorkFileUrls(availableCores);
        //Create worker object instance
        ParseWorker parseWorker = new ParseWorker(availableCores, workFilesUrls);
        //Invoke work to start
        parseWorker.doWork();
    }
}
