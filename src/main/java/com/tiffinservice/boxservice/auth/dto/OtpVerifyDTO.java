package com.tiffinservice.boxservice.auth.dto;

import lombok.Data;

@Data
public class OtpVerifyDTO {
    private String identifier;
    private String otp;
    private String deviceId;
}
