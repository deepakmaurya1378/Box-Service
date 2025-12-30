package com.tiffinservice.boxservice.vendor.dto;

import com.tiffinservice.boxservice.common.enums.DocumentVerificationStatus;
import com.tiffinservice.boxservice.common.enums.MealType;
import com.tiffinservice.boxservice.common.enums.VendorStatus;
import com.tiffinservice.boxservice.common.enums.VendorType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class VendorResponseDTO {
    private Long id;
    private String email;
    private String vendorName;
    private String ownerName;
    private String phone;
    private VendorStatus status;
    private VendorType vendorType;
    private DocumentVerificationStatus documentVerificationStatus;
    private Boolean sellingUnderCompanyName;
    private String verificationNotes;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pincode;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal deliveryRadiusKm;
    private BigDecimal commissionRate;
    private BigDecimal averageRating;
    private Integer totalOrders;
    private List<MealType> availableMeals;
    private LocalTime breakfastStartTime;
    private LocalTime breakfastEndTime;
    private LocalTime lunchStartTime;
    private LocalTime lunchEndTime;
    private LocalTime dinnerStartTime;
    private LocalTime dinnerEndTime;
    private String adharCard;
    private String panCard;
    private String fssaiLicense;
    private String fssaiLicenseNumber;
    private String gstNumber;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
