package com.tiffinservice.boxservice.admin.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private String phone;

    private Boolean isActive;
    private Boolean isBlocked;
    private Boolean isEmailVerified;
    private Boolean isPhoneVerified;

    private LocalDateTime createdAt;
}
