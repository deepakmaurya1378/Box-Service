package com.tiffinservice.boxservice.user.mapper;


import com.tiffinservice.boxservice.user.dto.UserVendorPreferenceResponseDTO;
import com.tiffinservice.boxservice.user.entity.UserVendorPreference;


public class UserVendorPreferenceMapper {


    public static UserVendorPreferenceResponseDTO toDTO(UserVendorPreference pref) {
        return UserVendorPreferenceResponseDTO.builder()
                .id(pref.getId())
                .userId(pref.getUser().getId())
                .vendorId(pref.getVendor().getId())
                .shiftType(pref.getShiftType())
                .preferenceType(pref.getPreferenceType())
                .build();
    }
}