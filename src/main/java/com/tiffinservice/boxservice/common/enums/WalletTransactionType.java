package com.tiffinservice.boxservice.common.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.tiffinservice.boxservice.common.utils.EnumUtil;

public enum WalletTransactionType {
    CREDIT,
    DEBIT,
    REFUND;

    @JsonCreator
    public static WalletTransactionType from(String value) {
        return EnumUtil.fromString(WalletTransactionType.class, value);
    }
}
