package com.tiffinservice.boxservice.user.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequestDTO {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone must be numeric and between 10-15 digits")
    private String phone;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private String profileImageUrl;

    private LocalDate dob;

    private String gender;
}
