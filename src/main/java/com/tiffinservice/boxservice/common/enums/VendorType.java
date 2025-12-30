package com.tiffinservice.boxservice.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.tiffinservice.boxservice.common.utils.EnumUtil;

public enum VendorType {
    LICENSED ,
    NON_LICENSED;

    @JsonCreator
    public static VendorType from(String value) {
        return EnumUtil.fromString(VendorType.class, value);
    }

    }
