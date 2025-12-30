package com.tiffinservice.boxservice.user.service;

import com.tiffinservice.boxservice.user.dto.UserVendorPreferenceRequestDTO;
import com.tiffinservice.boxservice.user.dto.UserVendorPreferenceResponseDTO;
import com.tiffinservice.boxservice.common.enums.ShiftType;
import java.util.List;

public interface UserVendorPreferenceService {
    UserVendorPreferenceResponseDTO addVendorPreference(UserVendorPreferenceRequestDTO request);
    List<UserVendorPreferenceResponseDTO> getPreferencesByShift(Long userId, ShiftType shiftType);
    void removeVendorPreference(Long userId, Long vendorId, ShiftType shiftType);
    UserVendorPreferenceResponseDTO changeDefaultVendor(Long userId, Long vendorId, ShiftType shiftType);
    UserVendorPreferenceResponseDTO removeDefaultVendor(Long userId, Long vendorId, ShiftType shiftType);
}
