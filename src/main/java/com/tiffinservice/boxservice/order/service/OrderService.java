package com.tiffinservice.boxservice.order.service;

import com.tiffinservice.boxservice.order.dto.ManualOrderRequestDTO;
import com.tiffinservice.boxservice.order.dto.OrderResponseDTO;

import java.util.List;

public interface OrderService {

    OrderResponseDTO createManualOrder(Long userId, ManualOrderRequestDTO dto);

    List<OrderResponseDTO> getUserOrders(Long userId);
}
