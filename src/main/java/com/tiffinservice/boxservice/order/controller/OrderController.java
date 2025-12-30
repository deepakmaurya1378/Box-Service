package com.tiffinservice.boxservice.order.controller;

import com.tiffinservice.boxservice.order.dto.ManualOrderRequestDTO;
import com.tiffinservice.boxservice.order.dto.OrderResponseDTO;
import com.tiffinservice.boxservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/manual/{userId}")
    public OrderResponseDTO createManualOrder(
            @PathVariable Long userId,
            @RequestBody ManualOrderRequestDTO dto
    ) {
        return orderService.createManualOrder(userId, dto);
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponseDTO> getUserOrders(@PathVariable Long userId) {
        return orderService.getUserOrders(userId);
    }

}
