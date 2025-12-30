package com.tiffinservice.boxservice.order.mapper;

import com.tiffinservice.boxservice.order.entity.Order;
import com.tiffinservice.boxservice.order.dto.OrderResponseDTO;

public class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponseDTO toResponse(Order order) {
        if (order == null) {
            return null;
        }

        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .orderType(order.getOrderType())
                .totalAmount(order.getTotalAmount())
                .confirmedAt(order.getConfirmedAt())
                .userId(order.getUser().getId())
                .userName(order.getUser().getFullName())
                .vendorId(order.getVendor().getId())
                .vendorName(order.getVendor().getVendorName())
                .build();
    }
}
