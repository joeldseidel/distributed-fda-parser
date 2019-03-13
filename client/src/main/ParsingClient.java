package main;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ParsingClient {
    public static void main(String args[]){
        //Attempt to connect with the server
        ServerConnection conn = connectToSession();
        if(conn != null){
            //TODO: this
        }
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
}
