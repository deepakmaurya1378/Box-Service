package com.tiffinservice.boxservice.vendor.mapper;

import com.tiffinservice.boxservice.vendor.dto.VendorRequestDTO;
import com.tiffinservice.boxservice.vendor.dto.VendorResponseDTO;
import com.tiffinservice.boxservice.vendor.entity.Vendor;
import org.springframework.stereotype.Component;

@Component
public class VendorMapper {

    public Vendor toEntity(VendorRequestDTO dto, Vendor vendor) {

        vendor.setEmail(dto.getEmail());
        vendor.setVendorName(dto.getVendorName());
        vendor.setOwnerName(dto.getOwnerName());
        vendor.setPhone(dto.getPhone());
        vendor.setVendorType(dto.getVendorType());
        vendor.setSellingUnderCompanyName(dto.getSellingUnderCompanyName());

        vendor.setAddressLine1(dto.getAddressLine1());
        vendor.setAddressLine2(dto.getAddressLine2());
        vendor.setCity(dto.getCity());
        vendor.setState(dto.getState());
        vendor.setPincode(dto.getPincode());

        vendor.setLatitude(dto.getLatitude());
        vendor.setLongitude(dto.getLongitude());
        vendor.setDeliveryRadiusKm(dto.getDeliveryRadiusKm());
        vendor.setCommissionRate(dto.getCommissionRate());

        vendor.setAvailableMeals(dto.getAvailableMeals());

        vendor.setBreakfastStartTime(dto.getBreakfastStartTime());
        vendor.setBreakfastEndTime(dto.getBreakfastEndTime());
        vendor.setLunchStartTime(dto.getLunchStartTime());
        vendor.setLunchEndTime(dto.getLunchEndTime());
        vendor.setDinnerStartTime(dto.getDinnerStartTime());
        vendor.setDinnerEndTime(dto.getDinnerEndTime());

        vendor.setAdharCard(dto.getAdharCard());
        vendor.setPanCard(dto.getPanCard());
        vendor.setFssaiLicense(dto.getFssaiLicense());
        vendor.setFssaiLicenseNumber(dto.getFssaiLicenseNumber());
        vendor.setGstNumber(dto.getGstNumber());

        return vendor;
    }

    public VendorResponseDTO toDTO(Vendor vendor) {
        VendorResponseDTO dto = new VendorResponseDTO();
        dto.setId(vendor.getId());
        dto.setEmail(vendor.getEmail());
        dto.setVendorName(vendor.getVendorName());
        dto.setOwnerName(vendor.getOwnerName());
        dto.setPhone(vendor.getPhone());
        dto.setStatus(vendor.getStatus());
        dto.setVendorType(vendor.getVendorType());
        dto.setDocumentVerificationStatus(vendor.getDocumentVerificationStatus());
        dto.setSellingUnderCompanyName(vendor.getSellingUnderCompanyName());
        dto.setVerificationNotes(vendor.getVerificationNotes());
        dto.setAddressLine1(vendor.getAddressLine1());
        dto.setAddressLine2(vendor.getAddressLine2());
        dto.setCity(vendor.getCity());
        dto.setState(vendor.getState());
        dto.setPincode(vendor.getPincode());
        dto.setLatitude(vendor.getLatitude());
        dto.setLongitude(vendor.getLongitude());
        dto.setDeliveryRadiusKm(vendor.getDeliveryRadiusKm());
        dto.setCommissionRate(vendor.getCommissionRate());
        dto.setAverageRating(vendor.getAverageRating());
        dto.setTotalOrders(vendor.getTotalOrders());
        dto.setAvailableMeals(vendor.getAvailableMeals());
        dto.setBreakfastStartTime(vendor.getBreakfastStartTime());
        dto.setBreakfastEndTime(vendor.getBreakfastEndTime());
        dto.setLunchStartTime(vendor.getLunchStartTime());
        dto.setLunchEndTime(vendor.getLunchEndTime());
        dto.setDinnerStartTime(vendor.getDinnerStartTime());
        dto.setDinnerEndTime(vendor.getDinnerEndTime());
        dto.setAdharCard(vendor.getAdharCard());
        dto.setPanCard(vendor.getPanCard());
        dto.setFssaiLicense(vendor.getFssaiLicense());
        dto.setFssaiLicenseNumber(vendor.getFssaiLicenseNumber());
        dto.setGstNumber(vendor.getGstNumber());
        dto.setIsActive(vendor.getIsActive());
        dto.setCreatedAt(vendor.getCreatedAt());
        dto.setUpdatedAt(vendor.getUpdatedAt());
        return dto;
    }
}
