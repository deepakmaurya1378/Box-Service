package com.tiffinservice.boxservice.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.tiffinservice.boxservice.common.utils.EnumUtil;

public enum MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACKS,
    DESSERT;

    @JsonCreator
    public static MealType from(String value) {
        return EnumUtil.fromString(MealType.class, value);
    }
}
