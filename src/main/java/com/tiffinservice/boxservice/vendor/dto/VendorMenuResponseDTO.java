package com.tiffinservice.boxservice.vendor.dto;

import com.tiffinservice.boxservice.common.enums.DayOfWeek;
import com.tiffinservice.boxservice.common.enums.MealType;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorMenuResponseDTO {

    private Long menuId;
    private Long vendorId;

    private MealType mealType;
    private DayOfWeek dayOfWeek;
    private Boolean isDaily;

    private String mealTitle;
    private String description;
    private BigDecimal price;
    private String category;
    private String imageUrl;

    private Boolean isAvailable;
    private Integer availableQuantity;
}
