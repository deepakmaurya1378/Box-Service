package com.tiffinservice.boxservice.admin.dto;

import com.tiffinservice.boxservice.user.entity.UserVendorPreference;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPreferenceDTO {

    private Long vendorId;
    private String vendorName;
    private String shiftType;
    private String preferenceType;

    public static UserPreferenceDTO from(UserVendorPreference pref) {
        return UserPreferenceDTO.builder()
                .vendorId(pref.getVendor().getId())
                .vendorName(pref.getVendor().getVendorName())
                .shiftType(pref.getShiftType().name())
                .preferenceType(pref.getPreferenceType().name())
                .build();
    }
}
