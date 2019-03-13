package main;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

class ParseWorker {
    private int cores;
    private BlockingQueue<String> workFileQueue = new LinkedBlockingQueue<>();

    ParseWorker(int cores, String[] workFileUrls){
        this.cores = cores;
        //Enqueue the files from the file url array
        for (String fileUrl : workFileUrls) {
            try {
                workFileQueue.put(fileUrl);
            } catch (InterruptedException iEx) {
                iEx.printStackTrace();
            }
        }
    }

    /**
     * Send a file to its executor and invoke parsing
     */
    public void doWork(){
        //Create thread pool executor object
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(this.cores);
    }
}
