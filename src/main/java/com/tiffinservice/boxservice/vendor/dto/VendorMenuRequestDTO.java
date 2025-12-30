package com.tiffinservice.boxservice.vendor.dto;


import com.tiffinservice.boxservice.common.enums.DayOfWeek;
import com.tiffinservice.boxservice.common.enums.MealType;
import lombok.*;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorMenuRequestDTO {

    private Long vendorId;

    private String mealTitle;
    private String description;
    private BigDecimal price;
    private String category;
    private String imageUrl;
    private Integer availableQuantity;

    private MealType mealType;
    private DayOfWeek dayOfWeek;

    private Boolean isDaily;
}
