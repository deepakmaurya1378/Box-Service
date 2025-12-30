package com.tiffinservice.boxservice.auth.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AppRole {
    ADMIN,
    USER,
    VENDOR;

    @JsonCreator
    public static AppRole from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        return AppRole.valueOf(value.trim().toUpperCase());
    }
}
