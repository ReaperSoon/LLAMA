package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by bonini_j on 26/10/2014.
 */
public class Hash {
    private static MessageDigest digest;
    static  {
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            // Nothing to do (must never happened
        }
    }

    public static String sha1(String clearPassword) {
        return byteArrayToHexString(digest.digest(clearPassword.getBytes()));
    }

    private static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (byte aB : b) {
            result +=
                    Integer.toString((aB & 0xff) + 0x100, 16).substring(1);
        }

        return result;
    }
}
