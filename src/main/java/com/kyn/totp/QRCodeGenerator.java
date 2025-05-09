package com.kyn.totp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

/**
 * Utility class for generating QR codes.
 * This class provides methods for generating QR codes and converting them to various formats.
 */
public class QRCodeGenerator {
    private static final String CHARSET = "UTF-8";
    private static final String IMAGE_FORMAT = "PNG";

    /**
     * Generates a QR code image and saves it to a file.
     * @param text The text to encode in the QR code
     * @param width The width of the QR code image
     * @param height The height of the QR code image
     * @param filePath The path where the QR code image will be saved
     * @throws WriterException If there is an error generating the QR code
     * @throws IOException If there is an error saving the image
     */
    public static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
        MatrixToImageWriter.writeToPath(bitMatrix, IMAGE_FORMAT, new File(filePath).toPath());
    }

    /**
     * Generates a QR code image and returns it as a base64 encoded string.
     * @param text The text to encode in the QR code
     * @param width The width of the QR code image
     * @param height The height of the QR code image
     * @return Base64 encoded QR code image
     * @throws WriterException If there is an error generating the QR code
     * @throws IOException If there is an error processing the image
     */
    public static String getQRCodeImageBase64(String text, int width, int height)
            throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), IMAGE_FORMAT, outputStream);
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }
}
