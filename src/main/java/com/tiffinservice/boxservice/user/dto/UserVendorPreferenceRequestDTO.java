
package com.tiffinservice.boxservice.user.dto;


import com.tiffinservice.boxservice.common.enums.PreferenceType;
import com.tiffinservice.boxservice.common.enums.ShiftType;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVendorPreferenceRequestDTO {
    private Long userId;
    private Long vendorId;
    private ShiftType shiftType;
    private PreferenceType preferenceType;
}