package com.tiffinservice.boxservice.admin.service.impl;

import com.tiffinservice.boxservice.vendor.dto.VendorResponseDTO;
import com.tiffinservice.boxservice.vendor.entity.Vendor;
import com.tiffinservice.boxservice.common.enums.DocumentVerificationStatus;
import com.tiffinservice.boxservice.common.enums.VendorStatus;
import com.tiffinservice.boxservice.vendor.mapper.VendorMapper;
import com.tiffinservice.boxservice.vendor.repository.VendorRepository;
import com.tiffinservice.boxservice.admin.service.VendorManagementService;
import com.tiffinservice.boxservice.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorManagementServiceImpl implements VendorManagementService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    @Override
    public VendorResponseDTO approveVendor(Long vendorId) {
        Vendor vendor = getVendorOrThrow(vendorId);

        vendor.setStatus(VendorStatus.APPROVED);
        vendor.setVerificationNotes(null);

        Vendor updatedVendor = vendorRepository.save(vendor);
        return vendorMapper.toDTO(updatedVendor);
    }

    @Override
    public VendorResponseDTO rejectVendor(Long vendorId, String notes) {
        Vendor vendor = getVendorOrThrow(vendorId);

        vendor.setStatus(VendorStatus.REJECTED);
        vendor.setVerificationNotes(notes);

        Vendor updatedVendor = vendorRepository.save(vendor);
        return vendorMapper.toDTO(updatedVendor);
    }

    @Override
    public VendorResponseDTO suspendVendor(Long vendorId) {
        Vendor vendor = getVendorOrThrow(vendorId);

        vendor.setStatus(VendorStatus.SUSPENDED);

        Vendor updatedVendor = vendorRepository.save(vendor);
        return vendorMapper.toDTO(updatedVendor);
    }

    @Override
    public List<VendorResponseDTO> getPendingVendors() {
        return vendorRepository.findByStatus(VendorStatus.PENDING)
                .stream()
                .map(vendorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VendorResponseDTO> getDocumentPendingVendors() {
        return vendorRepository.findByDocumentVerificationStatus(DocumentVerificationStatus.PENDING)
                .stream()
                .map(vendorMapper::toDTO)
                .collect(Collectors.toList());
    }


    private Vendor getVendorOrThrow(Long vendorId) {
        return vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vendor not found with id: " + vendorId)
                );
    }
}
