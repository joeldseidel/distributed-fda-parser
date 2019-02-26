package main.Managers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SessionInstanceManager {
    //This is the code entered into the client to register with this session
    private static String sessionCode;
    private static final int sessionCodeLength = 6;
    //Numeric session key generated to verify the workers
    private static long sessionKey;
    //List of the registered clients check sums
    private static List<Long> workerCheckSums = new ArrayList<>();

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

    /**
     * Determine if session code argument is valid
     * @param checkCode provided session code to check
     * @return validity of the check code
     */
    public static boolean isValidSessionCode(String checkCode) {
        return checkCode.equals(sessionCode);
    }

    /**
     * Attach client as a worker. That is - calculate a check sum for this client
     * @param verificationKey long generated by the client for server handshake
     */
    public static void attachClient(long verificationKey) {
        //Calculate user check sum and add to attached client check sum list
        workerCheckSums.add(sessionKey - verificationKey);
    }

    /**
     * Determine if client has been attached to the session
     * @param verificationKey verification key of the connecting client
     * @return if client is attached or not
     */
    public static boolean isAttachedClient(long verificationKey) {
        return workerCheckSums.contains(sessionKey - verificationKey);
    }
}
