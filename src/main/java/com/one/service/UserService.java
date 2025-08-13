package com.one.service;

import com.one.entities.Otp;
import com.one.entities.User;
import com.one.repositories.OtpRepository;
import com.one.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    public void register(String mobile) {
        String cleanedMobile = cleanMobile(mobile);
//        if (userRepository.existsByMobile(cleanedMobile)) {
//            throw new RuntimeException("Mobile number already registered");
//        }
        User user = new User();
        user.setMobile(cleanedMobile);
        userRepository.save(user);
    }


    public void saveOtp(String mobile, String otp) {
        String cleanedMobile = cleanMobile(mobile);

        User user = userRepository.findByMobile(cleanedMobile)
                .orElseGet(() -> userRepository.save(new User(cleanedMobile)));

        Otp newOtp = new Otp(user, otp, LocalDateTime.now());
        otpRepository.save(newOtp);
    }

    public List<Otp> getOtps(String mobile) {
        return otpRepository.findByUserMobile(cleanMobile(mobile));
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

    public ResponseEntity<String> getLatestOtp(String mobile) {
        return otpRepository.findTopByUserMobileOrderByTimestampDesc(mobile).map(otp -> ResponseEntity.ok(otp.getOtp()))
                .orElse(ResponseEntity.notFound().build());
    }

    // Clean expired OTPs
    @Transactional
    @Scheduled(fixedRateString = "${otp.cleanup.fixedRate:120000}")
    public void cleanupExpiredOtps() {
        System.out.println("Clear OTP is triggered");
        otpRepository.deleteExpired(LocalDateTime.now().minusMinutes(2));
    }
}

