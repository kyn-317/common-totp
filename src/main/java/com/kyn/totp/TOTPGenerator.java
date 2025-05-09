package com.kyn.totp;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;

/**
 * Utility class for generating and verifying TOTP (Time-based One-Time Password) codes.
 * This class provides methods for generating secret keys and TOTP codes,
 * as well as verifying TOTP codes.
 *
 * @author Kyn
 * @version 1.0
 */
public class TOTPGenerator {
    private static final int DIGITS = 6;
    private static final int TIME_STEP = 30;

    /**
     * Private constructor to prevent instantiation.
     */
    private TOTPGenerator() {
        // Utility class
    }

    /**
     * Generates a random secret key for TOTP generation.
     * @return A Base32 encoded secret key
     */
    public static String generateSecretKey() {
        byte[] key = new byte[20];
        new java.security.SecureRandom().nextBytes(key);
        return new Base32().encodeToString(key);
    }

    /**
     * Generates a TOTP code using the provided secret key.
     * @param secretKey The Base32 encoded secret key
     * @return A 6-digit TOTP code
     */
    public static String getTOTPCode(String secretKey) {
        try {
            byte[] key = new Base32().decode(secretKey);
            long timeStep = Instant.now().getEpochSecond() / TIME_STEP;
            byte[] timeStepBytes = ByteBuffer.allocate(8).putLong(timeStep).array();

            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA1");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(timeStepBytes);

            int offset = hash[hash.length - 1] & 0xf;
            int binary = ((hash[offset] & 0x7f) << 24) |
                    ((hash[offset + 1] & 0xff) << 16) |
                    ((hash[offset + 2] & 0xff) << 8) |
                    (hash[offset + 3] & 0xff);

            int otp = binary % (int) Math.pow(10, DIGITS);
            return String.format("%0" + DIGITS + "d", otp);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error generating TOTP code", e);
        }
    }

    /**
     * Verifies if the provided TOTP code is valid for the given secret key.
     * @param secretKey The Base32 encoded secret key
     * @param code The TOTP code to verify
     * @return true if the code is valid, false otherwise
     */
    public static boolean verifyTOTPCode(String secretKey, String code) {
        String currentCode = getTOTPCode(secretKey);
        return currentCode.equals(code);
    }
}
