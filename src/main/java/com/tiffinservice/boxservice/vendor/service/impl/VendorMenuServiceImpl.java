package com.tiffinservice.boxservice.vendor.service.impl;

import com.tiffinservice.boxservice.common.enums.*;
import com.tiffinservice.boxservice.common.exception.BadRequestException;
import com.tiffinservice.boxservice.common.exception.ResourceNotFoundException;
import com.tiffinservice.boxservice.vendor.dto.VendorMenuRequestDTO;
import com.tiffinservice.boxservice.vendor.dto.VendorMenuResponseDTO;
import com.tiffinservice.boxservice.vendor.entity.Vendor;
import com.tiffinservice.boxservice.vendor.entity.VendorMenu;
import com.tiffinservice.boxservice.vendor.mapper.VendorMenuMapper;
import com.tiffinservice.boxservice.vendor.repository.VendorMenuRepository;
import com.tiffinservice.boxservice.vendor.repository.VendorRepository;
import com.tiffinservice.boxservice.vendor.service.VendorMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorMenuServiceImpl implements VendorMenuService {

    private final VendorMenuRepository menuRepository;
    private final VendorRepository vendorRepository;

    @Override
    public VendorMenuResponseDTO createMenu(VendorMenuRequestDTO dto) {

        Vendor vendor = vendorRepository.findById(dto.getVendorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vendor not found with id: " + dto.getVendorId()));

        validateMenuRequest(dto);

        if (Boolean.FALSE.equals(dto.getIsDaily())) {

            boolean exists =
                    menuRepository.existsByVendor_IdAndMealTypeAndDayOfWeek(
                            dto.getVendorId(),
                            dto.getMealType(),
                            dto.getDayOfWeek()
                    );

            if (exists) {
                throw new BadRequestException(
                        "Menu already exists for "
                                + dto.getDayOfWeek()
                                + " "
                                + dto.getMealType()
                                + ". Only one menu is allowed per day per meal."
                );
            }
        }

        VendorMenu menu = VendorMenuMapper.toEntity(dto, vendor);
        return VendorMenuMapper.toDTO(menuRepository.save(menu));
    }

    @Override
    public VendorMenuResponseDTO updateMenu(Long menuId, VendorMenuRequestDTO dto) {
        VendorMenu menu = menuRepository.findById(menuId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Menu not found with id: " + menuId));

        validateMenuRequest(dto);

        boolean isDaily = Boolean.TRUE.equals(dto.getIsDaily());

        if (!isDaily) {

            boolean exists =
                    menuRepository.existsByVendor_IdAndMealTypeAndDayOfWeek(
                            menu.getVendor().getId(),
                            dto.getMealType(),
                            dto.getDayOfWeek()
                    );


            boolean sameMenu =
                    menu.getMealType() == dto.getMealType()
                            && menu.getDayOfWeek() == dto.getDayOfWeek();

            if (exists && !sameMenu) {
                throw new BadRequestException(
                        "Another menu already exists for "
                                + dto.getDayOfWeek()
                                + " "
                                + dto.getMealType()
                );
            }
        }

        menu.setMealTitle(dto.getMealTitle());
        menu.setDescription(dto.getDescription());
        menu.setPrice(dto.getPrice());
        menu.setCategory(dto.getCategory());
        menu.setImageUrl(dto.getImageUrl());
        menu.setAvailableQuantity(dto.getAvailableQuantity());
        menu.setIsDaily(isDaily);

        if (isDaily) {
            menu.setMealType(null);
            menu.setDayOfWeek(null);
        } else {
            menu.setMealType(dto.getMealType());
            menu.setDayOfWeek(dto.getDayOfWeek());
        }

        return VendorMenuMapper.toDTO(menuRepository.save(menu));
    }

    @Override
    public void deleteMenu(Long menuId) {

        VendorMenu menu = menuRepository.findById(menuId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Menu not found with id: " + menuId));

        menuRepository.delete(menu);
    }


    @Override
    public List<VendorMenuResponseDTO> getMenuByVendor(Long vendorId) {

        if (!vendorRepository.existsById(vendorId)) {
            throw new ResourceNotFoundException(
                    "Vendor not found with id: " + vendorId);
        }

        return menuRepository.findByVendor_Id(vendorId)
                .stream()
                .map(VendorMenuMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<VendorMenuResponseDTO> getMenuByVendorAndMeal(
            Long vendorId,
            MealType mealType
    ) {

        if (!vendorRepository.existsById(vendorId)) {
            throw new ResourceNotFoundException(
                    "Vendor not found with id: " + vendorId);
        }

        return menuRepository.findByVendor_IdAndMealType(vendorId, mealType)
                .stream()
                .map(VendorMenuMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VendorMenuResponseDTO> getMenuForDayAndMeal(
            Long vendorId,
            MealType mealType,
            DayOfWeek dayOfWeek
    ) {

        if (!vendorRepository.existsById(vendorId)) {
            throw new ResourceNotFoundException(
                    "Vendor not found with id: " + vendorId);
        }

        List<VendorMenu> result = new ArrayList<>();
        result.addAll(menuRepository.findByVendor_IdAndIsDailyTrue(vendorId));

        result.addAll(menuRepository
                .findByVendor_IdAndMealTypeAndDayOfWeek(
                        vendorId,
                        mealType,
                        dayOfWeek
                ));

        return result.stream()
                .map(VendorMenuMapper::toDTO)
                .collect(Collectors.toList());
    }


    private void validateMenuRequest(VendorMenuRequestDTO dto) {
        if (dto.getIsDaily() == null) { throw new BadRequestException("isDaily field is required");}

        if (Boolean.FALSE.equals(dto.getIsDaily())) {
            if (dto.getMealType() == null || dto.getDayOfWeek() == null) {
                throw new BadRequestException(
                        "mealType and dayOfWeek are required for non-daily menu");
            }
        }
    }
}
