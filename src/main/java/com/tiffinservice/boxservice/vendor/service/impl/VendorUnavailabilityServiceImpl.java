package com.tiffinservice.boxservice.vendor.service.impl;

import com.tiffinservice.boxservice.common.exception.VendorLeaveNotAllowedException;
import com.tiffinservice.boxservice.vendor.entity.Vendor;
import com.tiffinservice.boxservice.vendor.entity.VendorUnavailability;
import com.tiffinservice.boxservice.vendor.repository.VendorUnavailabilityRepository;
import com.tiffinservice.boxservice.vendor.service.VendorUnavailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorUnavailabilityServiceImpl
        implements VendorUnavailabilityService {

    private static final LocalTime LEAVE_CUTOFF_TIME = LocalTime.of(2, 0);

    private final VendorUnavailabilityRepository repo;

    @Override
    public boolean isVendorUnavailable(Long vendorId, LocalDate date) {
        return repo.existsByVendor_IdAndDate(vendorId, date);
    }

    @Override
    public VendorUnavailability markUnavailable(Vendor vendor, LocalDate date, String reason) {
        validateLeaveTiming(date);

        return repo.save(
                VendorUnavailability.builder()
                        .vendor(vendor)
                        .date(date)
                        .reason(reason)
                        .build()
        );
    }

    @Override
    public void removeUnavailable(Long vendorId, LocalDate date) {
        validateLeaveTiming(date);
        repo.deleteByVendor_IdAndDate(vendorId, date);
    }

    @Override
    public List<VendorUnavailability> getVendorUnavailability(Long vendorId) {
        return repo.findByVendor_Id(vendorId);
    }

    private void validateLeaveTiming(LocalDate leaveDate) {

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if (leaveDate.isEqual(today) && now.isAfter(LEAVE_CUTOFF_TIME)) {
            throw new VendorLeaveNotAllowedException(
                    "You cannot mark leave today! Please plan your leave in advance."
            );
        }
    }
}
