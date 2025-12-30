package com.tiffinservice.boxservice.order.service;

import com.tiffinservice.boxservice.user.entity.User;
import com.tiffinservice.boxservice.vendor.entity.Vendor;
import com.tiffinservice.boxservice.common.enums.ShiftType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AutoOrderService {
    void createAutoOrder(
            User user,
            Vendor vendor,
            ShiftType shiftType,
            LocalDate orderDate,
            LocalDateTime scheduledAt
    );

}
