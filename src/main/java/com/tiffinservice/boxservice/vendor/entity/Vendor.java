package com.tiffinservice.boxservice.vendor.entity;

import com.tiffinservice.boxservice.common.enums.DocumentVerificationStatus;
import com.tiffinservice.boxservice.common.enums.MealType;
import com.tiffinservice.boxservice.common.enums.VendorStatus;
import com.tiffinservice.boxservice.common.enums.VendorType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, length = 120)
    private String vendorName;

    @Column(nullable = false, length = 120)
    private String ownerName;

    @Column(unique = true, nullable = false, length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VendorStatus status = VendorStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private VendorType vendorType = VendorType.LICENSED;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private DocumentVerificationStatus documentVerificationStatus = DocumentVerificationStatus.NOT_REQUIRED;

    private Boolean sellingUnderCompanyName = false;

    @Column(length = 500)
    private String verificationNotes;

    @Column(nullable = false, length = 200)
    private String addressLine1;

    @Column(length = 200)
    private String addressLine2;

    @Column(nullable = false, length = 60)
    private String city;

    @Column(nullable = false)
    private Boolean isOpen = false;

    @Column(nullable = false, length = 60)
    private String state;

    @Column(nullable = false, length = 10)
    private String pincode;

    @Column(nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal deliveryRadiusKm = BigDecimal.valueOf(5.00);

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal commissionRate = BigDecimal.valueOf(15.00);

    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal averageRating = BigDecimal.valueOf(0.00);

    @Column(nullable = false)
    private Integer totalOrders = 0;

    @Column(nullable = false, length = 12)
    private String adharCard;

    @Column(nullable = false, length = 10)
    private String panCard;

    @Column(length = 20)
    private String fssaiLicense;

    @Column(length = 20)
    private String fssaiLicenseNumber;

    @Column(length = 15)
    private String gstNumber;

    private Boolean isActive = true;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private List<MealType> availableMeals;

    private LocalTime breakfastStartTime;
    private LocalTime breakfastEndTime;

    private LocalTime lunchStartTime;
    private LocalTime lunchEndTime;

    private LocalTime dinnerStartTime;
    private LocalTime dinnerEndTime;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
