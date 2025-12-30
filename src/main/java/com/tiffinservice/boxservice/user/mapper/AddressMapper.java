package com.tiffinservice.boxservice.user.mapper;

import com.tiffinservice.boxservice.user.dto.AddressRequestDTO;
import com.tiffinservice.boxservice.user.dto.AddressResponseDTO;
import com.tiffinservice.boxservice.user.entity.Address;
import com.tiffinservice.boxservice.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toEntity(AddressRequestDTO dto, User user) {
        if (dto == null || user == null) return null;
        return Address.builder()
                .user(user)
                .addressType(dto.getAddressType())
                .addressLine1(dto.getAddressLine1())
                .addressLine2(dto.getAddressLine2())
                .landmark(dto.getLandmark())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry())
                .postalCode(dto.getPostalCode())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .isPrimary(dto.getIsPrimary() != null ? dto.getIsPrimary() : false)
                .build();
    }

    public AddressResponseDTO toDto(Address address) {
        if (address == null) return null;
        return AddressResponseDTO.builder()
                .id(address.getId())
                .userId(address.getUser().getId())
                .addressType(address.getAddressType())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .landmark(address.getLandmark())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .isPrimary(address.getIsPrimary())
                .createdAt(address.getCreatedAt())
                .updatedAt(address.getUpdatedAt())
                .build();
    }
}
