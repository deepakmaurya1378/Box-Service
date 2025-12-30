package com.tiffinservice.boxservice.common.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.tiffinservice.boxservice.common.utils.EnumUtil;

public enum PreferenceType {
    DEFAULT,
    PREFERRED;

    @JsonCreator
    public static PreferenceType from(String value) {
        return EnumUtil.fromString(PreferenceType.class, value);
    }

}