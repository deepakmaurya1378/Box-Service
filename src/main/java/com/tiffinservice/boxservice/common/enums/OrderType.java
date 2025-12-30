package com.tiffinservice.boxservice.common.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.tiffinservice.boxservice.common.utils.EnumUtil;

public enum OrderType {
    AUTO,
    MANUAL;


    @JsonCreator
    public static OrderType from(String value) {
        return EnumUtil.fromString(OrderType.class, value);
    }

}
