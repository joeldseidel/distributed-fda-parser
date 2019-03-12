package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ParsingClient {
    public static void main(String args[]){
        if(connectToSession()){
            initWorker();
        }
    }

    private static boolean connectToSession(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Maverick Software Systems FDA Data Parsing Client");
        System.out.println("\n\n\n");
        System.out.println("Enter server URL: ");
        String serverUrl = "";
        try{
            serverUrl = br.readLine();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            return false;
        }
        System.out.println("Enter the session code: ");
        String sessionCode = "";
        try{
            sessionCode = br.readLine();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            return false;
        }

    }

    private static void initWorker(){

    }
}
