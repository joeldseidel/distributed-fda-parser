package main.Managers;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProcessedDataManager {
    private static BlockingQueue<String> processedDataQueue = new LinkedBlockingQueue<>();
    public static void addProcessedData(String processedData) throws InterruptedException{
        processedDataQueue.put(processedData);
    }
}
