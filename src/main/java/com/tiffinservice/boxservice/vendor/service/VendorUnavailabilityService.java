package com.tiffinservice.boxservice.vendor.service;

import com.tiffinservice.boxservice.vendor.entity.Vendor;
import com.tiffinservice.boxservice.vendor.entity.VendorUnavailability;

import java.time.LocalDate;
import java.util.List;

public interface VendorUnavailabilityService {

    boolean isVendorUnavailable(Long vendorId, LocalDate date);

    VendorUnavailability markUnavailable(Vendor vendor, LocalDate date, String reason);

    void removeUnavailable(Long vendorId, LocalDate date);

    List<VendorUnavailability> getVendorUnavailability(Long vendorId);
}
