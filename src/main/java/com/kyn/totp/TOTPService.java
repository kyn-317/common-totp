package com.kyn.totp;

import java.io.IOException;

import com.google.zxing.WriterException;

/**
 * Service class for handling TOTP (Time-based One-Time Password) operations.
 * This class provides functionality for generating and verifying TOTP codes,
 * as well as generating QR codes for TOTP setup.
 *
 * @author Kyn
 * @version 1.0
 */
public class TOTPService {
    private final String SECRET_KEY;
    private final String APP_NAME;
    private static final String QRCODE_STRING_FORMAT = "otpauth://totp/%s:%s?secret=%s&amp;issuer=%s";

    /**
     * Creates a new TOTPService instance with a randomly generated secret key.
     */
    public TOTPService() {
        this.SECRET_KEY = TOTPGenerator.generateSecretKey();
        this.APP_NAME = "TOTP";
    }

    /**
     * Creates a new TOTPService instance with the specified secret key.
     * @param secretKey The secret key to use for TOTP generation
     */
    public TOTPService(String secretKey) {
        this.SECRET_KEY = secretKey;
        this.APP_NAME = "TOTP";
    }

    /**
     * Creates a new TOTPService instance with the specified secret key and application name.
     * @param secretKey The secret key to use for TOTP generation
     * @param appName The name of the application for QR code generation
     */
    public TOTPService(String secretKey, String appName) {
        this.SECRET_KEY = secretKey;
        this.APP_NAME = appName;
    }

    /**
     * Generates the TOTP setup text for QR code generation.
     * @param loginId The user's login ID
     * @param secretKey The secret key for TOTP generation
     * @return The TOTP setup text in the format "otpauth://totp/{appName}:{loginId}?secret={secretKey}&amp;issuer={appName}"
     */
    public String getQrText(String loginId, String secretKey) {
        return String.format(QRCODE_STRING_FORMAT, APP_NAME, loginId, secretKey, APP_NAME);
    }

    /**
     * Generates a QR code image in base64 format for the given login ID.
     * @param loginId The user's login ID
     * @return Base64 encoded QR code image
     * @throws WriterException If there is an error generating the QR code
     * @throws IOException If there is an error processing the image
     */
    public String getQrCodeImageBase64(String loginId) throws WriterException, IOException {
        return getQRCodeImageBase64(getQrText(loginId, SECRET_KEY), 400, 400);
    }

    /**
     * Generates a QR code image in base64 format with custom dimensions.
     * @param text The text to encode in the QR code
     * @param width The width of the QR code image
     * @param height The height of the QR code image
     * @return Base64 encoded QR code image
     * @throws WriterException If there is an error generating the QR code
     * @throws IOException If there is an error processing the image
     */
    public String getQRCodeImageBase64(String text, int width, int height) throws WriterException, IOException {
        return QRCodeGenerator.getQRCodeImageBase64(text, width, height);
    }

    /**
     * Gets the secret key used for TOTP generation.
     * @return The secret key
     */
    public String getSecretKey() {
        return SECRET_KEY;
    }

    /**
     * Generates a TOTP code using the current time and secret key.
     * @return The generated TOTP code
     */
    public String getTOTPCode() {
        return TOTPGenerator.getTOTPCode(SECRET_KEY);
    }

    /**
     * Verifies if the provided TOTP code is valid.
     * @param code The TOTP code to verify
     * @return true if the code is valid, false otherwise
     */
    public boolean verifyTOTPCode(String code) {
        return TOTPGenerator.verifyTOTPCode(SECRET_KEY, code);
    }
}
