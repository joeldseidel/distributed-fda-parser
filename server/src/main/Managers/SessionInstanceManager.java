package main.Managers;

import java.util.Random;

public class SessionInstanceManager {
    private static long sessionKey;
    private static long[] workerCheckSums;

    /**
     * Set the unique ID of this session
     */
    public static void generateSessionKey() {
        Random sessionKeyGen = new Random();
        sessionKey = sessionKeyGen.nextLong();
    }
}
