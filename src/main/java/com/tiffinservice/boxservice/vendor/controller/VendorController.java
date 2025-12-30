package com.tiffinservice.boxservice.vendor.controller;

import com.tiffinservice.boxservice.order.dto.OrderResponseDTO;
import com.tiffinservice.boxservice.vendor.dto.*;
import com.tiffinservice.boxservice.vendor.service.VendorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

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

    @PostMapping("/{vendorId}/availability")
    public ResponseEntity<VendorAvailabilityResponseDTO> toggleAvailability(@PathVariable Long vendorId, @RequestBody @Valid VendorToggleAvailabilityDTO dto) {

        vendorService.toggleVendor(vendorId, dto.getIsOpen());
        return ResponseEntity.ok(
                VendorAvailabilityResponseDTO.builder()
                        .vendorId(vendorId)
                        .isOpen(dto.getIsOpen())
                        .message(dto.getIsOpen() ? "Vendor opened successfully" : "Vendor closed successfully")
                        .build()
        );
    }

    @GetMapping("/{vendorId}/orders/today")
    public ResponseEntity<List<OrderResponseDTO>> getTodayOrders(@PathVariable Long vendorId) {
        return ResponseEntity.ok(vendorService.getTodayOrders(vendorId));
    }

    @GetMapping("/{vendorId}/orders/today/summary")
    public ResponseEntity<VendorTodayOrderSummaryDTO> getTodayOrderSummary(@PathVariable Long vendorId) {
        return ResponseEntity.ok(vendorService.getTodayOrderSummary(vendorId));
    }




}
