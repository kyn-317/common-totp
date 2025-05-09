package com.kyn.totp;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.zxing.WriterException;

public class TOTPServiceTest {
    private static final String TEST_SECRET_KEY = "JBSWY3DPEHPK3PXP";
    private TOTPService totpService;

    @BeforeEach
    void setUp() {
        totpService = new TOTPService(TEST_SECRET_KEY);
    }

    @Test
    void testDefaultConstructor() {
        TOTPService service = new TOTPService();
        assertNotNull(service.getSecretKey());
        assertTrue(service.getSecretKey().length() > 0);
    }

    @Test
    void testGetSecretKey() {
        assertEquals(TEST_SECRET_KEY, totpService.getSecretKey());
    }

    @Test
    void testGetTOTPCode() {
        String totpCode = totpService.getTOTPCode();
        assertNotNull(totpCode);
        assertEquals(6, totpCode.length());
        assertTrue(totpCode.matches("\\d{6}"));
    }

    @Test
    void testVerifyTOTPCode() {
        String validCode = totpService.getTOTPCode();
        assertTrue(totpService.verifyTOTPCode(validCode));
        assertFalse(totpService.verifyTOTPCode("000000"));
    }

    @Test
    void testGetQRCodeImageBase64() throws WriterException, IOException {
        String qrCode = totpService.getQrCodeImageBase64("Test QR Code");
        System.out.println(qrCode);
        assertNotNull(qrCode);
    }

    @Test
    void testGetQRCodeImageBase64WithCustomSize() throws WriterException, IOException {
        String qrCode = totpService.getQRCodeImageBase64("Test QR Code", 300, 300);
        System.out.println(qrCode);
        assertNotNull(qrCode);
    }
} 