package com.tiffinservice.boxservice.vendor.controller;

import com.tiffinservice.boxservice.vendor.dto.VendorRequestDTO;
import com.tiffinservice.boxservice.vendor.dto.VendorResponseDTO;
import com.tiffinservice.boxservice.vendor.dto.VendorUpdateDTO;
import com.tiffinservice.boxservice.vendor.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @PostMapping("/register")
    public ResponseEntity<VendorResponseDTO> registerVendor(
            @RequestBody VendorRequestDTO vendorRequestDTO) {
        return ResponseEntity.ok(vendorService.RegisterVendor(vendorRequestDTO));
    }

    @GetMapping("/{vendorId}")
    public ResponseEntity<VendorResponseDTO> getVendorById(@PathVariable Long vendorId) {
        return ResponseEntity.ok(vendorService.getVendorById(vendorId));
    }

    @PatchMapping("/{vendorId}")
    public ResponseEntity<VendorResponseDTO> updateVendor(
            @PathVariable Long vendorId,
            @RequestBody VendorUpdateDTO dto) {
        return ResponseEntity.ok(vendorService.updateVendor(vendorId, dto));
    }

    @DeleteMapping("/{vendorId}")
    public ResponseEntity<Void> deleteVendor(@PathVariable Long vendorId) {
        vendorService.deleteVendor(vendorId);
        return ResponseEntity.noContent().build();
    }
}
