package main.Managers;

import java.util.concurrent.ThreadLocalRandom;

public class SessionInstanceManager {
    //This is the code entered into the client to register with this session
    private static String sessionCode;
    private static final int sessionCodeLength = 6;
    //Numeric session key generated to verify the workers
    private static long sessionKey;
    //List of the registered clients check sums
    private static long[] workerCheckSums;

    /**
     * Generate session code string
     */
    public static String generateSessionCode() {
        //62 code character candidates
        final String codeCandidates = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
        StringBuilder codeBuilder = new StringBuilder(sessionCodeLength);
        for(int i = 0; i < 6; i++){
            //Add a new random character from the candidates string
            codeBuilder.append(codeCandidates.charAt(ThreadLocalRandom.current().nextInt(codeCandidates.length())));
        }
        sessionCode = codeBuilder.toString();
        return sessionCode;
    }

    /**
     * Set the unique ID of this session
     */
    public static void generateSessionKey() {
        sessionKey = ThreadLocalRandom.current().nextLong();
    }
}
