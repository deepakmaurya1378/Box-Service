package com.tiffinservice.boxservice.admin.dto;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponseDTO {
    private Long vendorId;
    private String vendorName;
    private String status;
    private String notes;
}