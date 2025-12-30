package com.tiffinservice.boxservice.order.dto;

import com.tiffinservice.boxservice.common.enums.OrderStatus;
import com.tiffinservice.boxservice.common.enums.OrderType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponseDTO {

    private Long orderId;
    private OrderStatus status;
    private OrderType orderType;
    private BigDecimal totalAmount;
    private LocalDateTime confirmedAt;

    private Long userId;
    private String userName;

    private Long vendorId;
    private String vendorName;
}
