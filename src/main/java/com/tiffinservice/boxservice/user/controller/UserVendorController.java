package com.tiffinservice.boxservice.user.controller;

import com.tiffinservice.boxservice.common.enums.DayOfWeek;
import com.tiffinservice.boxservice.common.enums.MealType;
import com.tiffinservice.boxservice.vendor.dto.NearbyVendorResponseDTO;
import com.tiffinservice.boxservice.vendor.dto.VendorMenuResponseDTO;
import com.tiffinservice.boxservice.vendor.dto.VendorResponseDTO;
import com.tiffinservice.boxservice.vendor.service.VendorMenuService;
import com.tiffinservice.boxservice.vendor.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users/vendors")
@RequiredArgsConstructor
public class UserVendorController {

    private final VendorService vendorService;
    private final VendorMenuService vendorMenuService;

    @GetMapping("/{vendorId}")
    public VendorResponseDTO getVendorDetails(@PathVariable Long vendorId) {
        return vendorService.getVendorById(vendorId);
    }

    @GetMapping("/nearby")
    public List<NearbyVendorResponseDTO> getNearbyVendors(@RequestParam Long addressId) {
        return vendorService.getNearbyDeliverableVendors(addressId);
    }

    @GetMapping("/nearby/meal")
    public List<NearbyVendorResponseDTO> getNearbyVendorsByMeal(@RequestParam Long addressId, @RequestParam MealType mealType) {
        return vendorService.getNearbyDeliverableVendors(addressId, mealType);
    }

    @GetMapping("/{vendorId}/menu")
    public List<VendorMenuResponseDTO> getVendorMenu(@PathVariable Long vendorId) {
        return vendorMenuService.getMenuByVendor(vendorId);
    }

    @GetMapping("/{vendorId}/menu/day-meal")
    public List<VendorMenuResponseDTO> getVendorMenuForTodayAndMeal(@PathVariable Long vendorId, @RequestParam MealType mealType) {
        DayOfWeek today = DayOfWeek.valueOf(LocalDate.now().getDayOfWeek().name());
        return vendorMenuService.getMenuForDayAndMeal(vendorId, mealType, today);
    }


    @GetMapping("/{vendorId}/menu/meal/{mealType}")
    public List<VendorMenuResponseDTO> getVendorMenuByMeal(@PathVariable Long vendorId, @PathVariable MealType mealType) {
        return vendorMenuService.getMenuByVendorAndMeal(vendorId, mealType);
    }
}
