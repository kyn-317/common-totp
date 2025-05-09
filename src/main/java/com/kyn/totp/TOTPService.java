package com.kyn.totp;

import java.io.IOException;

import com.google.zxing.WriterException;

public class TOTPService {
    private final String SECRET_KEY;
    private final String APP_NAME;
    private static final String QRCODE_STRING_FORMAT = "otpauth://totp/%s:%s?secret=%s&issuer=%s";
    public TOTPService() {
        this.SECRET_KEY = TOTPGenerator.generateSecretKey();
        this.APP_NAME = "TOTP";
    }
    public TOTPService(String secretKey) {
        this.SECRET_KEY = secretKey;
        this.APP_NAME = "TOTP";
    }
    public TOTPService(String secretKey, String appName) {
        this.SECRET_KEY = secretKey;
        this.APP_NAME = appName;
    }

    public String getQrText(String loginId, String secretKey){
        return String.format(QRCODE_STRING_FORMAT, APP_NAME, loginId, secretKey, APP_NAME);
    }

    public String getQrCodeImageBase64(String loginId) throws WriterException, IOException {
        return getQRCodeImageBase64(getQrText(loginId, SECRET_KEY), 400, 400);
    }

    public String getQRCodeImageBase64(String text, int width, int height) throws WriterException, IOException {
        return QRCodeGenerator.getQRCodeImageBase64(text, width, height);
    }

    public String getSecretKey() {
        return SECRET_KEY;
    }

    public String getTOTPCode() {
        return TOTPGenerator.getTOTPCode(SECRET_KEY);
    }

    public boolean verifyTOTPCode(String code) {
        return TOTPGenerator.verifyTOTPCode(SECRET_KEY, code);
    }

}
