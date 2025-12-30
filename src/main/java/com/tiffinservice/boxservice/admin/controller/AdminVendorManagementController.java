package com.tiffinservice.boxservice.admin.controller;

import com.tiffinservice.boxservice.admin.dto.RejectVendorRequestDTO;
import com.tiffinservice.boxservice.vendor.dto.VendorResponseDTO;
import com.tiffinservice.boxservice.admin.service.VendorManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/vendors")
@RequiredArgsConstructor
public class AdminVendorManagementController {

    private final VendorManagementService adminVendorService;

    @PatchMapping("/{vendorId}/approve")
    public ResponseEntity<VendorResponseDTO> approveVendor(
            @PathVariable Long vendorId
    ) {
        return ResponseEntity.ok(
                adminVendorService.approveVendor(vendorId)
        );
    }

    @PatchMapping("/{vendorId}/reject")
    public ResponseEntity<VendorResponseDTO> rejectVendor(
            @PathVariable Long vendorId,
            @RequestBody RejectVendorRequestDTO request
    ) {
        return ResponseEntity.ok(
                adminVendorService.rejectVendor(vendorId, request.getNotes())
        );
    }

    @PatchMapping("/{vendorId}/suspend")
    public ResponseEntity<VendorResponseDTO> suspendVendor(
            @PathVariable Long vendorId
    ) {
        return ResponseEntity.ok(
                adminVendorService.suspendVendor(vendorId)
        );
    }

    @GetMapping("/pending")
    public ResponseEntity<List<VendorResponseDTO>> getPendingVendors() {
        return ResponseEntity.ok(
                adminVendorService.getPendingVendors()
        );
    }

    @GetMapping("/document-pending")
    public ResponseEntity<List<VendorResponseDTO>> getDocumentPendingVendors() {
        return ResponseEntity.ok(
                adminVendorService.getDocumentPendingVendors()
        );
    }
}
