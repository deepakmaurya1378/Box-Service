package com.tiffinservice.boxservice.user.dto;

import com.tiffinservice.boxservice.common.enums.AddressType;
import com.tiffinservice.boxservice.user.entity.Address;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequestDTO {
    private Long userId;
    private AddressType addressType;
    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean isPrimary;
}
