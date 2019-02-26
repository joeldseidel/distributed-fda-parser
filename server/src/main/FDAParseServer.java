package main;

import com.sun.net.httpserver.HttpServer;
import main.Handlers.AuthenticateClientHandler;
import main.Handlers.ErrorReportHandler;
import main.Handlers.TaskReportHandler;
import main.Handlers.TaskRequestHandler;
import main.Managers.SessionInstanceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

public class FDAParseServer {
    public static void main(String args[]){
        if(startSession()){
            startServer();
        }
    }


    private static boolean startSession(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Start a new parse session? (Y / n): ");
        boolean startNewSession = false;
        try {
            startNewSession = br.readLine().equals("Y");
        } catch (IOException ioEx) {
            System.out.println("Invalid input");
        }
        if(!startNewSession){
            return false;
        }
        //Set key for this session
        SessionInstanceManager.generateSessionKey();
        return true;
    }

    private static void startServer(){
        try{
            HttpServer server = HttpServer.create(new InetSocketAddress(6699), 0);
            //Authenticate that the client is supposed to be connected
            //Give it a key to verify itself by
            server.createContext("/authenticate_client", new AuthenticateClientHandler());
            //Client is authenticated and now wants a task
            //This handler is for when a client is requesting a new file to parse
            server.createContext("/request_task", new TaskRequestHandler());
            //Client has completed a task
            //This handler is for when a client says it finished its task
            server.createContext("/report_task", new TaskReportHandler());
            //Client needs to report an error
            //This handler will handle if a client fails to complete its task for some reason
            server.createContext("/report_error", new ErrorReportHandler());
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }
}
