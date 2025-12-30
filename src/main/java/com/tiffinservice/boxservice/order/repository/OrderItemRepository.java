package com.tiffinservice.boxservice.order.repository;

import com.tiffinservice.boxservice.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
