package com.tiffinservice.boxservice.auth.dto;

import com.tiffinservice.boxservice.auth.enums.AppRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {

    private String accessToken;
    private Long userId;
    private AppRole role;
}
