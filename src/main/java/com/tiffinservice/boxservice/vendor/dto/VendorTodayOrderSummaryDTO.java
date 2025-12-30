package com.tiffinservice.boxservice.vendor.dto;

import com.tiffinservice.boxservice.order.dto.OrderResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VendorTodayOrderSummaryDTO {


    private long breakfastOrders;
    private long lunchOrders;
    private long dinnerOrders;

    private long totalManualOrders;

    private List<OrderResponseDTO> manualOrders;
    private List<OrderResponseDTO> autoOrders;
}
