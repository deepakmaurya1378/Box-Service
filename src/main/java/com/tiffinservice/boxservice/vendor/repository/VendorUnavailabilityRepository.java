package com.tiffinservice.boxservice.vendor.repository;

import com.tiffinservice.boxservice.vendor.entity.VendorUnavailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VendorUnavailabilityRepository
        extends JpaRepository<VendorUnavailability, Long> {

    boolean existsByVendor_IdAndDate(Long vendorId, LocalDate date);

    List<VendorUnavailability> findByVendor_Id(Long vendorId);

    void deleteByVendor_IdAndDate(Long vendorId, LocalDate date);
}
