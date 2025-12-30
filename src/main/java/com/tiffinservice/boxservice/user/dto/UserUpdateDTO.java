package com.tiffinservice.boxservice.user.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDTO {

    private String fullName;
    private String profileImageUrl;
    private LocalDate dob;
    private String gender;
}
