package com.tiffinservice.boxservice.admin.dto;

import com.tiffinservice.boxservice.user.entity.Address;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressDTO {

    private Long id;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private Boolean isPrimary;

    public static UserAddressDTO from(Address address) {
        return UserAddressDTO.builder()
                .id(address.getId())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .isPrimary(address.getIsPrimary())
                .build();
    }
}
