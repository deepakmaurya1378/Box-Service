package com.tiffinservice.boxservice.user.entity;

import com.tiffinservice.boxservice.vendor.entity.Vendor;
import com.tiffinservice.boxservice.common.enums.PreferenceType;
import com.tiffinservice.boxservice.common.enums.ShiftType;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "user_vendor_preference",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "vendor_id", "shift_type"})
)
public class UserVendorPreference {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShiftType shiftType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PreferenceType preferenceType;
}