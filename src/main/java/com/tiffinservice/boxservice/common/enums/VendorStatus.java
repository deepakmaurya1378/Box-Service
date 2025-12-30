package com.tiffinservice.boxservice.common.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.tiffinservice.boxservice.common.utils.EnumUtil;

public enum VendorStatus {
    PENDING, APPROVED, REJECTED, SUSPENDED;

    @JsonCreator
    public static VendorStatus from(String value) {
        return EnumUtil.fromString(VendorStatus.class, value);
    }

}
