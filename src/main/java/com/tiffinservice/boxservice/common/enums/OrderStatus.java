package com.tiffinservice.boxservice.common.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.tiffinservice.boxservice.common.utils.EnumUtil;

public enum OrderStatus {
    CREATED,
    CONFIRMED,
    CANCELLED,
    DELIVERED,
    FAILED;

    @JsonCreator
    public static OrderStatus from(String value) {
        return EnumUtil.fromString(OrderStatus.class, value);
    }

}
