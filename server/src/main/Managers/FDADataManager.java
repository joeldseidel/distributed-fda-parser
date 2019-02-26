package main.Managers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FDADataManager {
    private static BlockingQueue<String> fileQueue = new LinkedBlockingQueue<>();
    private static final String gudidDownloadListUrl = "https://api.fda.gov/download.json";

    public static boolean fetchAvailableFiles(){
        URL fdaFileDataFile;
        try{
            fdaFileDataFile = new URL(gudidDownloadListUrl);
        } catch (MalformedURLException muEx) {
            muEx.printStackTrace();
            return false;
        }
        HttpURLConnection httpConn;
        int fdaResponseCode;
        try{
            httpConn = (HttpURLConnection)fdaFileDataFile.openConnection();
            fdaResponseCode = httpConn.getResponseCode();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            return false;
        }
        if(fdaResponseCode != HttpURLConnection.HTTP_OK){
            System.out.println("FDA response code: " + fdaResponseCode);
            return false;
        }
        StringBuilder fileJsonStringBuilder = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String thisLine;
            while((thisLine = br.readLine()) != null){
                fileJsonStringBuilder.append(thisLine);
            }
            br.close();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            return false;
        }
        JSONObject fdaFileUrlObj = new JSONObject(fileJsonStringBuilder.toString());
        JSONArray fileJsonArray = fdaFileUrlObj.getJSONObject("results").getJSONObject("device").getJSONObject("udi").getJSONArray("partitions");
        System.out.println("Found " + fileJsonArray.length() + " files to parse");
        try {
            for(int i = 0; i < fileJsonArray.length(); i++) {
                fileQueue.put(fileJsonArray.getJSONObject(i).getString("file"));
            }
        } catch (InterruptedException iEx) {
            iEx.printStackTrace();
            return false;
        }
        return true;
    }
}
