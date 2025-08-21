package com.one.utils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class OcrUtils {

    public static String extractTextFromBase64(String base64Image) throws IOException {
        // Clean prefix, if present
        if (base64Image.startsWith("data:")) {
            base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
        }
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        // Use .png extension to match original format
        File temp = File.createTempFile("captcha", ".png");
        Files.write(temp.toPath(), imageBytes);

        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("/opt/homebrew/share/tessdata");
        tesseract.setLanguage("eng");


        try {
            return tesseract.doOCR(temp);
        } catch (TesseractException e) {
            throw new IOException("OCR failed", e);
        } finally {
            // More reliable temp file cleanup
            temp.delete();
        }
    }

}

