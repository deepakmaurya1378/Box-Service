package com.tiffinservice.boxservice.order.repository;

import com.tiffinservice.boxservice.order.entity.Order;
import com.tiffinservice.boxservice.user.entity.User;
import com.tiffinservice.boxservice.common.enums.OrderType;
import com.tiffinservice.boxservice.common.enums.ShiftType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    boolean existsByUser_IdAndOrderDateAndShiftTypeAndOrderType(
            Long userId,
            LocalDate orderDate,
            ShiftType shiftType,
            OrderType orderType
    );
}

