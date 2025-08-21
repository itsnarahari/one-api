package com.one.service;

import com.one.entities.Otp;
import com.one.entities.User;
import com.one.repositories.OtpRepository;
import com.one.repositories.UserRepository;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Value("${otp.cleanup.expiryMillis:60000}")
    private long expiryMillis;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Value("${ocr.tessdata-dir:/opt/homebrew/share/tessdata}")
    private String tessdataDir;

    @Value("${ocr.lang:eng}")
    private String lang;

    private ITesseract newEngine() {
        Tesseract t = new Tesseract();
        t.setDatapath(tessdataDir);
        t.setLanguage(lang);
        t.setPageSegMode(7); // Treat image as a single text line
        return t;
    }

    public void register(String mobile) {
        String cleanedMobile = cleanMobile(mobile);
        User user = new User();
        user.setMobile(cleanedMobile);
        userRepository.save(user);
    }


    public void saveOtp(String mobile, String otp) {
        Otp newOtp = new Otp(cleanMobile(mobile), otp, LocalDateTime.now());
        otpRepository.save(newOtp);
    }

    public List<Otp> getOtps(String mobile) {
        return otpRepository.findByMobile(cleanMobile(mobile));
    }

    private String cleanMobile(String mobile) {
        mobile = mobile.replaceAll("[^0-9]", "");
        if (mobile.startsWith("91") && mobile.length() > 10) {
            mobile = mobile.substring(mobile.length() - 10);
        }
        if (mobile.length() != 10) {
            throw new IllegalArgumentException("Invalid mobile number");
        }
        return mobile;
    }

    public ResponseEntity<List<String>> getLatestOtp(String mobile) {
        return ResponseEntity.ok(otpRepository.findAllByMobileOrderByTimestampDesc(mobile).stream()
                .map(Otp::getOtp).toList());
    }

    @Transactional
    @Scheduled(fixedRateString = "${otp.cleanup.fixedRate:60000}")
    public void scheduleToClearOtp() {
        cleanupExpiredOtps();
    }

    public void cleanupExpiredOtps() {
        log.info("Started OTP clearing");
        LocalDateTime cutoff = LocalDateTime.now()
                .minusNanos(expiryMillis * 1_000_000);
        otpRepository.deleteExpired(cutoff);
        log.info("OTPs older than {} ms are cleared", expiryMillis);
    }

    @Transactional
    public void clearAllOtps() {
        log.info("Clearing All Otp's");
        otpRepository.deleteAll();
        log.info("All the Otp's are cleared");
    }

    public Map<String, List<String>> getOtpStringsGroupedByMobile() {
        List<Otp> otps = otpRepository.findAllOrderByMobileAndTimestampDesc();
        return otps.stream()
                .collect(Collectors.groupingBy(
                        Otp::getMobile,
                        Collectors.mapping(Otp::getOtp, Collectors.toList())
                ));
    }

    public String extractFromBase64(String dataUriOrBase64) throws IOException {
        String payload = dataUriOrBase64;
        int comma = payload.indexOf(',');
        if (payload.startsWith("data:") && comma > -1) {
            payload = payload.substring(comma + 1);
        }
        byte[] bytes = Base64.getMimeDecoder().decode(payload);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
        if (img == null) throw new IllegalArgumentException("Invalid image data");
        try {
            String captcha = newEngine().doOCR(img);
            String filtered = captcha.replaceAll("[^A-Z0-9]", ""); // keep only uppercase letters and digits

            return !filtered.isBlank()?filtered.replaceAll(" ",""):null;
        } catch (TesseractException e) {
            throw new RuntimeException("OCR failed", e);
        }
    }

}

