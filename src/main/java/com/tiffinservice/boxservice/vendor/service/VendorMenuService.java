package com.tiffinservice.boxservice.vendor.service;

import com.tiffinservice.boxservice.common.enums.MealType;
import com.tiffinservice.boxservice.common.enums.DayOfWeek;
import com.tiffinservice.boxservice.vendor.dto.VendorMenuRequestDTO;
import com.tiffinservice.boxservice.vendor.dto.VendorMenuResponseDTO;

import java.util.List;

public interface VendorMenuService {

    VendorMenuResponseDTO createMenu(VendorMenuRequestDTO dto);
    VendorMenuResponseDTO updateMenu(Long menuId, VendorMenuRequestDTO dto);
    void deleteMenu(Long menuId);
    List<VendorMenuResponseDTO> getMenuByVendor(Long vendorId);
    List<VendorMenuResponseDTO> getMenuByVendorAndMeal(Long vendorId, MealType mealType);
    List<VendorMenuResponseDTO> getMenuForDayAndMeal(Long vendorId, MealType mealType, DayOfWeek dayOfWeek);
}
