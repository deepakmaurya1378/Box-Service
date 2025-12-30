package com.tiffinservice.boxservice.vendor.controller;

import com.tiffinservice.boxservice.vendor.entity.Vendor;
import com.tiffinservice.boxservice.vendor.entity.VendorUnavailability;
import com.tiffinservice.boxservice.vendor.repository.VendorRepository;
import com.tiffinservice.boxservice.vendor.service.VendorUnavailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vendor/unavailability")
@RequiredArgsConstructor
public class VendorUnavailabilityController {

    private final VendorRepository vendorRepo;
    private final VendorUnavailabilityService service;

    @PostMapping("/{vendorId}")
    public VendorUnavailability markUnavailable(
            @PathVariable Long vendorId,
            @RequestParam LocalDate date,
            @RequestParam(required = false) String reason
    ) {
        Vendor vendor = vendorRepo.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        return service.markUnavailable(vendor, date, reason);
    }

    @DeleteMapping("/{vendorId}")
    public void removeUnavailable(
            @PathVariable Long vendorId,
            @RequestParam LocalDate date
    ) {
        service.removeUnavailable(vendorId, date);
    }

    // ✅ Get leave calendar
    @GetMapping("/{vendorId}")
    public List<VendorUnavailability> getUnavailability(
            @PathVariable Long vendorId
    ) {
        return service.getVendorUnavailability(vendorId);
    }
}
