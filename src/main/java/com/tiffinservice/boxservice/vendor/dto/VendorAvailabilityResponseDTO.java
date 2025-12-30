package com.tiffinservice.boxservice.vendor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VendorAvailabilityResponseDTO {

    private Long vendorId;
    private Boolean isOpen;
    private String message;
}
