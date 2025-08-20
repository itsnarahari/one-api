package com.one.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.one.configs.LocalDateTimeFromEpochDeserializer;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class OtpRequest {
    @NotBlank
    private String mobile;
    @NotBlank
    private String otp;
    @JsonDeserialize(using = LocalDateTimeFromEpochDeserializer.class)
    private LocalDateTime timestamp;
    private String purpose;
    private String sender;
    private String smsBody;

    public @NotBlank String getMobile() {
        return mobile;
    }

    public void setMobile(@NotBlank String mobile) {
        this.mobile = mobile;
    }

    public @NotBlank String getOtp() {
        return otp;
    }

    public void setOtp(@NotBlank String otp) {
        this.otp = otp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSmsBody() {
        return smsBody;
    }

    public void setSmsBody(String smsBody) {
        this.smsBody = smsBody;
    }
}
