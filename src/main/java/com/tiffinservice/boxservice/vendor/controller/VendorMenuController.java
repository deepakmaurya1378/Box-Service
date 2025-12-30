package com.tiffinservice.boxservice.vendor.controller;

import com.tiffinservice.boxservice.common.enums.DayOfWeek;
import com.tiffinservice.boxservice.common.enums.MealType;
import com.tiffinservice.boxservice.vendor.dto.VendorMenuRequestDTO;
import com.tiffinservice.boxservice.vendor.dto.VendorMenuResponseDTO;
import com.tiffinservice.boxservice.vendor.service.VendorMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/menu")
@RequiredArgsConstructor
public class VendorMenuController {

    private final VendorMenuService menuService;
    @PostMapping
    public VendorMenuResponseDTO createMenu(@RequestBody VendorMenuRequestDTO dto) {
        return menuService.createMenu(dto);
    }

    @PatchMapping("/{menuId}")
    public VendorMenuResponseDTO updateMenu(@PathVariable Long menuId, @RequestBody VendorMenuRequestDTO dto) {
        return menuService.updateMenu(menuId, dto);
    }

    @DeleteMapping("/{menuId}")
    public void deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
    }

    @GetMapping("/vendor/{vendorId}")
    public List<VendorMenuResponseDTO> getMenuByVendor(@PathVariable Long vendorId) {
        return menuService.getMenuByVendor(vendorId);
    }

    @GetMapping("/vendor/{vendorId}/meal/{mealType}")
    public List<VendorMenuResponseDTO> getMenuByVendorAndMeal(@PathVariable Long vendorId,  @PathVariable MealType mealType) {
        return menuService.getMenuByVendorAndMeal(vendorId, mealType);
    }

    @GetMapping("/vendor/{vendorId}/day-meal")
    public List<VendorMenuResponseDTO> getMenuForDayAndMeal(@PathVariable Long vendorId, @RequestParam MealType mealType, @RequestParam DayOfWeek dayOfWeek) {
        return menuService.getMenuForDayAndMeal(
                vendorId,
                mealType,
                dayOfWeek
        );
    }
}
