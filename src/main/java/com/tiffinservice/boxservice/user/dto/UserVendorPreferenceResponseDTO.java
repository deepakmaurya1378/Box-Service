package com.tiffinservice.boxservice.user.dto;

import com.tiffinservice.boxservice.common.enums.PreferenceType;
import com.tiffinservice.boxservice.common.enums.ShiftType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVendorPreferenceResponseDTO {
    private Long id;
    private Long userId;
    private Long vendorId;
    private ShiftType shiftType;
    private PreferenceType preferenceType;
}
