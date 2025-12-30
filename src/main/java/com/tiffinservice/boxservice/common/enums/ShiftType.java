package com.tiffinservice.boxservice.common.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.tiffinservice.boxservice.common.utils.EnumUtil;

public enum ShiftType {
    BREAKFAST,
    LUNCH,
    DINNER;

    @JsonCreator
    public static ShiftType from(String value) {
        return EnumUtil.fromString(ShiftType.class, value);
    }


}