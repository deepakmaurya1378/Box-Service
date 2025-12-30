package com.tiffinservice.boxservice.vendor.entity;

import com.tiffinservice.boxservice.common.enums.DayOfWeek;
import com.tiffinservice.boxservice.common.enums.MealType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(
        name = "vendor_menu",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"vendor_id", "mealTitle", "mealType", "dayOfWeek"}
        )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;


    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private String mealTitle;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    private String category; // Veg / Non-Veg / Beverage

    private String imageUrl;

    @Column(nullable = false)
    private Boolean isDaily = false;

    @Column(nullable = false)
    private Boolean isAvailable = true;

    private Integer availableQuantity = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
