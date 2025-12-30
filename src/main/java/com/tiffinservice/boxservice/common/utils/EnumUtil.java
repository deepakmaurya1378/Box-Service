package com.tiffinservice.boxservice.common.utils;

public class EnumUtil {

    public static <E extends Enum<E>> E fromString(Class<E> enumClass, String value) {
        if (value == null) {
            throw new IllegalArgumentException("Enum value cannot be null");
        }
        try {
            return Enum.valueOf(enumClass, value.trim().toUpperCase());
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Invalid value '" + value + "' for enum " + enumClass.getSimpleName()
            );
        }
    }
}
