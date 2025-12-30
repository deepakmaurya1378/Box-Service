package com.tiffinservice.boxservice.vendor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "vendor_unavailability",
        uniqueConstraints = @UniqueConstraint(columnNames = {"vendor_id", "date"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorUnavailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 255)
    private String reason;
}
