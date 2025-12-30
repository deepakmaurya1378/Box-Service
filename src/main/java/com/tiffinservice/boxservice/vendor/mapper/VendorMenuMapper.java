package com.tiffinservice.boxservice.vendor.mapper;


import com.tiffinservice.boxservice.vendor.dto.VendorMenuRequestDTO;
import com.tiffinservice.boxservice.vendor.dto.VendorMenuResponseDTO;
import com.tiffinservice.boxservice.vendor.entity.Vendor;
import com.tiffinservice.boxservice.vendor.entity.VendorMenu;


public class VendorMenuMapper {

    public static VendorMenu toEntity(VendorMenuRequestDTO dto, Vendor vendor) {

        boolean isDaily = Boolean.TRUE.equals(dto.getIsDaily());

        return VendorMenu.builder()
                .vendor(vendor)
                .mealTitle(dto.getMealTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .category(dto.getCategory())
                .imageUrl(dto.getImageUrl())
                .availableQuantity(dto.getAvailableQuantity())
                .isAvailable(true)
                .isDaily(isDaily)
                .mealType(isDaily ? null : dto.getMealType())
                .dayOfWeek(isDaily ? null : dto.getDayOfWeek())
                .build();
    }

    public static VendorMenuResponseDTO toDTO(VendorMenu menu) {
        return VendorMenuResponseDTO.builder()
                .menuId(menu.getMenuId())
                .vendorId(menu.getVendor().getId())
                .mealType(menu.getMealType())
                .dayOfWeek(menu.getDayOfWeek())
                .isDaily(menu.getIsDaily())
                .mealTitle(menu.getMealTitle())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .category(menu.getCategory())
                .imageUrl(menu.getImageUrl())
                .isAvailable(menu.getIsAvailable())
                .availableQuantity(menu.getAvailableQuantity())
                .build();
    }
}
