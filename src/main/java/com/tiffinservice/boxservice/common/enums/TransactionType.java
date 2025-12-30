package com.tiffinservice.boxservice.common.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.tiffinservice.boxservice.common.utils.EnumUtil;

public enum TransactionType {

    CREDIT,
    DEBIT,
    UPI;

    @JsonCreator
    public static TransactionType from(String value) {
        return EnumUtil.fromString(TransactionType.class, value);
    }

}
