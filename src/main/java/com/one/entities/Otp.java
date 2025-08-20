package com.one.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "otps")
@Data
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String otp;
    private LocalDateTime timestamp;
    private String mobile;

    public Otp(String mobile, String otp, LocalDateTime now) {
        this.mobile = mobile;
        this.otp = otp;
        this.timestamp=now;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Otp(){

    }
    public boolean isExpired() {
        return timestamp.plusMinutes(2).isBefore(LocalDateTime.now());
    }
}

