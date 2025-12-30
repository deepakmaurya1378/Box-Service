package com.tiffinservice.boxservice.vendor.dto;

import com.tiffinservice.boxservice.common.enums.MealType;
import lombok.Data;

@Data
public class NearbyVendorRequestDTO {
    private Long addressId;
    private MealType mealType;
}
