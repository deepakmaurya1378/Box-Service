package com.tiffinservice.boxservice.vendor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VendorToggleAvailabilityDTO {

    @NotNull(message = "isOpen flag is required")
    private Boolean isOpen;
}
