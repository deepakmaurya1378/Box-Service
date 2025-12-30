package com.tiffinservice.boxservice.order.entity;

import com.tiffinservice.boxservice.vendor.entity.VendorMenu;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Order order;

    @ManyToOne(optional = false)
    private VendorMenu menu;

    private String itemName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal itemTotal;
}
