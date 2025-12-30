package com.tiffinservice.boxservice.common.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.tiffinservice.boxservice.common.utils.EnumUtil;


public enum AdminRole {
    ADMIN;

    @JsonCreator
    public static AdminRole from(String value) {
        return EnumUtil.fromString(AdminRole.class, value);
    }

}