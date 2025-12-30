package com.tiffinservice.boxservice.user.dto;

import com.tiffinservice.boxservice.common.enums.ShiftType;
import lombok.Data;

@Data
public class RemovePreferenceRequestDTO {
    private Long userId;
    private Long vendorId;
    private ShiftType shiftType;
}
