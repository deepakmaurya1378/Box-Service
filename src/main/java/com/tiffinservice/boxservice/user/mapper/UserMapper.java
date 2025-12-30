package com.tiffinservice.boxservice.user.mapper;

import com.tiffinservice.boxservice.user.dto.UserCreateRequestDTO;
import com.tiffinservice.boxservice.user.dto.UserCreateResponseDTO;
import com.tiffinservice.boxservice.user.entity.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UserMapper {

    public User toEntity(UserCreateRequestDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .profileImageUrl(dto.getProfileImageUrl())
                .dob(dto.getDob())
                .gender(dto.getGender())
                .build();
    }

    public UserCreateResponseDTO toDto(User user) {
        if (user == null) return null;

        return UserCreateResponseDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .profileImageUrl(user.getProfileImageUrl())
                .dob(user.getDob())
                .gender(user.getGender())
                .isPhoneVerified(user.getIsPhoneVerified())
                .isEmailVerified(user.getIsEmailVerified())
                .walletBalance(
                        user.getWallet() != null
                                ? user.getWallet().getBalance()
                                : BigDecimal.ZERO
                )
                .isBlocked(user.getIsBlocked())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
