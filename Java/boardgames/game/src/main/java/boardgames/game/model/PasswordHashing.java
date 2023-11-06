package boardgames.game.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHashing {
    public static String hash(String password) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] bytes = sha256.digest(password.getBytes(StandardCharsets.UTF_8));
            return toHexString(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String toHexString(byte[] bytes) {
        char[] ret = new char[bytes.length * 2];

        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            int highNibble = v >>> 4;
            int lowNibble = v & 0x0f;
            ret[i * 2] = HEX_DIGITS[highNibble];
            ret[i * 2 + 1] = HEX_DIGITS[lowNibble];
        }

        return new String(ret);
    }
}
