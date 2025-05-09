package com.kyn.totp;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;

public class TOTPGenerator {

  private static final String HMAC_ALGO = "HmacSHA1";
  private static final int TIME_STEP_SECONDS = 30;
  

  public static String generateSecretKey() {
    Base32 base32 = new Base32();
    byte[] bytes = new byte[20];
    new java.security.SecureRandom().nextBytes(bytes);
    return base32.encodeToString(bytes);
  }

  public static String getTOTPCode(String secretKey) {
    long timeIndex =
        TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) / TIME_STEP_SECONDS;
    return generateTOTP(secretKey, timeIndex);
  }

  public static boolean verifyTOTPCode(String secretKey, String code) {
    long timeIndex =
        TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) / TIME_STEP_SECONDS;
    String generatedCode = generateTOTP(secretKey, timeIndex);
    return generatedCode.equals(code);
  }

  private static String generateTOTP(String secretKey, long timeIndex) {
    Base32 base32 = new Base32();
    byte[] key = base32.decode(secretKey);
    byte[] data = new byte[8];
    long value = timeIndex;
    for (int i = 7; value > 0; i--) {
      data[i] = (byte) (value & 0xFF);
      value >>= 8;
    }

    try {
      SecretKeySpec signKey = new SecretKeySpec(key, HMAC_ALGO);
      Mac mac = Mac.getInstance(HMAC_ALGO);
      mac.init(signKey);
      byte[] hash = mac.doFinal(data);

      int offset = hash[hash.length - 1] & 0xF;
      long truncatedHash = 0;
      for (int i = 0; i < 4; ++i) {
        truncatedHash <<= 8;
        truncatedHash |= (hash[offset + i] & 0xFF);
      }
      truncatedHash &= 0x7FFFFFFF;
      truncatedHash %= 1000000;

      return String.format("%06d", truncatedHash);
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new RuntimeException("Error generating TOTP", e);
    }
  }
}
