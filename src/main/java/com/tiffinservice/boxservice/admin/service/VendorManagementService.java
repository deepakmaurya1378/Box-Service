package com.tiffinservice.boxservice.admin.service;
import com.tiffinservice.boxservice.vendor.dto.VendorResponseDTO;

import java.util.List;


public interface VendorManagementService {
        VendorResponseDTO approveVendor(Long vendorId);
        VendorResponseDTO rejectVendor(Long vendorId, String notes);
        VendorResponseDTO suspendVendor(Long vendorId);
        List<VendorResponseDTO> getPendingVendors();
        List<VendorResponseDTO> getDocumentPendingVendors();
    }

