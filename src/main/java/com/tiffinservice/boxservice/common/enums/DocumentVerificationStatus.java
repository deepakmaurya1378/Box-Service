package com.tiffinservice.boxservice.common.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.tiffinservice.boxservice.common.utils.EnumUtil;

public enum DocumentVerificationStatus {
    PENDING,
    APPROVED,
    REJECTED,
    NOT_REQUIRED;

    @JsonCreator
    public static DocumentVerificationStatus from(String value) {
        return EnumUtil.fromString(DocumentVerificationStatus.class, value);
    }

}
