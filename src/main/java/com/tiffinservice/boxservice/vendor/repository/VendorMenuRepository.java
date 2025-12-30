package com.tiffinservice.boxservice.vendor.repository;

import com.tiffinservice.boxservice.common.enums.DayOfWeek;
import com.tiffinservice.boxservice.vendor.entity.VendorMenu;
import com.tiffinservice.boxservice.common.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface VendorMenuRepository extends JpaRepository<VendorMenu, Long> {

    List<VendorMenu> findByVendor_Id(Long vendorId);

    List<VendorMenu> findByVendor_IdAndMealType(Long vendorId, MealType mealType);

    List<VendorMenu> findByVendor_IdAndMealTypeAndDayOfWeek(
            Long vendorId,
            MealType mealType,
            DayOfWeek dayOfWeek
    );

    List<VendorMenu> findByVendor_IdAndIsDailyTrue(Long vendorId);

    boolean existsByVendor_IdAndMealTypeAndDayOfWeek(
            Long vendorId,
            MealType mealType,
            DayOfWeek dayOfWeek
    );
}
