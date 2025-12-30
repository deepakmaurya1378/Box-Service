package com.tiffinservice.boxservice.common.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.tiffinservice.boxservice.common.utils.EnumUtil;

public enum TransactionStatus {
    SUCCESS,
    FAILED,
    PENDING;

    @JsonCreator
    public static TransactionStatus from(String value) {
        return EnumUtil.fromString(TransactionStatus.class, value);
    }

}
