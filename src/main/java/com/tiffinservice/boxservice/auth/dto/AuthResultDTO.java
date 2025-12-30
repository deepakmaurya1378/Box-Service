package com.tiffinservice.boxservice.auth.dto;

import com.tiffinservice.boxservice.auth.enums.AppRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResultDTO {

    private String accessToken;
    private String refreshToken;
    private Long userId;
    private AppRole role;
}
