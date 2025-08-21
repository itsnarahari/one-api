package com.one.controller;

import com.one.dto.ApiResponse;
import com.one.dto.OtpRequest;
import com.one.dto.RegisterRequest;
import com.one.entities.Otp;
import com.one.service.UserService;
import com.one.utils.OcrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/one")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        userService.register(request.getMobile());
        return ResponseEntity.ok(new ApiResponse("success", true));
    }

    @PostMapping("/otp/store")
    public ResponseEntity<ApiResponse> saveOtp(@RequestBody OtpRequest request) {
        userService.saveOtp(request.getMobile(), request.getOtp());
        return ResponseEntity.ok(new ApiResponse("OTP saved", true));
    }

    @GetMapping("/otp/latest/{mobile}")
    public ResponseEntity<List<String>> getLatestOtp(@PathVariable String mobile) {
        return userService.getLatestOtp(mobile);
    }

    @GetMapping("/otps/{mobile}")
    public ResponseEntity<List<Otp>> getOtps(@PathVariable String mobile) {
        return ResponseEntity.ok(userService.getOtps(mobile));
    }

    @PostMapping("/ocr")
    public ResponseEntity<String> extractText(@RequestBody Map<String, String> body) throws IOException {
        String base64 = body.get("image");
        return ResponseEntity.ok(userService.extractFromBase64(base64));
    }

    @GetMapping("/otp/clear")
    public ResponseEntity<String> clear() {
        userService.clearAllOtps();
        return ResponseEntity.ok("OTPs are cleared");
    }

    @GetMapping("/otp/read-all")
    public ResponseEntity<Map<String, List<String>>> readAll() {
        return ResponseEntity.ok(userService.getOtpStringsGroupedByMobile());
    }
}

