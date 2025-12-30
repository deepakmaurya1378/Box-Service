package com.tiffinservice.boxservice.user.repository;

import com.tiffinservice.boxservice.user.entity.UserVendorPreference;
import com.tiffinservice.boxservice.common.enums.PreferenceType;
import com.tiffinservice.boxservice.common.enums.ShiftType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserVendorPreferenceRepository extends JpaRepository<UserVendorPreference, Long> {

    int countByUserIdAndShiftType(Long userId, ShiftType shiftType);

    boolean existsByUserIdAndShiftTypeAndPreferenceType(Long userId, ShiftType shiftType, PreferenceType preferenceType);

    List<UserVendorPreference> findByUserIdAndShiftType(Long userId, ShiftType shiftType);

    Optional<UserVendorPreference> findByUserIdAndVendorIdAndShiftType(Long userId, Long vendorId, ShiftType shiftType);

    List<UserVendorPreference> findByShiftTypeAndPreferenceType(
            ShiftType shiftType,
            PreferenceType preferenceType
    );
}
