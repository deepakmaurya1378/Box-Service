package com.tiffinservice.boxservice.order.dto;

import com.tiffinservice.boxservice.common.enums.ShiftType;
import lombok.Data;

import java.util.List;

@Data
public class ManualOrderRequestDTO {

    private Long vendorId;
    private Long addressId;
    private ShiftType shiftType;
    private List<Item> items;

    @Data
    public static class Item {
        private Long menuId;
        private Integer quantity;
    }
}
