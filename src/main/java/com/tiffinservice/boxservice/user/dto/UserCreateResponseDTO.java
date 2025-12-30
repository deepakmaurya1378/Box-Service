package com.tiffinservice.boxservice.user.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String profileImageUrl;
    private LocalDate dob;
    private String gender;
    private Boolean isPhoneVerified;
    private Boolean isEmailVerified;
    private BigDecimal walletBalance;
    private Boolean isBlocked;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

