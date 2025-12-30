package com.tiffinservice.boxservice.auth.dto;

import com.tiffinservice.boxservice.auth.enums.AppRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "Email/Mobile is required")
     private String identifier;

    @NotBlank(message = "Password is required")
    private String password;

    private AppRole role;

    private String deviceId;
}
