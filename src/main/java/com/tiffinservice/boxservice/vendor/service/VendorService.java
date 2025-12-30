package com.tiffinservice.boxservice.vendor.service;

import com.tiffinservice.boxservice.vendor.dto.NearbyVendorResponseDTO;
import com.tiffinservice.boxservice.vendor.dto.VendorRequestDTO;
import com.tiffinservice.boxservice.vendor.dto.VendorResponseDTO;
import com.tiffinservice.boxservice.common.enums.MealType;
import com.tiffinservice.boxservice.vendor.dto.VendorUpdateDTO;

import java.util.List;

public interface VendorService {

    VendorResponseDTO RegisterVendor(VendorRequestDTO vendorRequestDTO);

    VendorResponseDTO getVendorById(Long id);

    VendorResponseDTO updateVendor(Long id, VendorUpdateDTO dto);

    void deleteVendor(Long id);

    List<NearbyVendorResponseDTO> getNearbyDeliverableVendors(Long addressId, MealType mealType);

    List<NearbyVendorResponseDTO> getNearbyDeliverableVendors(Long addressId);

}
