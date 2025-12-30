package com.tiffinservice.boxservice.vendor.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NearbyVendorResponseDTO {

    private Long vendorId;
    private String vendorName;
    private String city;
    private BigDecimal distanceKm;
    private BigDecimal deliveryRadiusKm;
    private BigDecimal averageRating;
}
