package com.one.dto;

import com.one.utils.Utils;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String mobile;

    public @NotBlank String getMobile() {
        return Utils.normalizeMobile(mobile);
    }

    public void setMobile(@NotBlank String mobile) {
        this.mobile = Utils.normalizeMobile(mobile);
    }
}

