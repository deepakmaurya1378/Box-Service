package com.tiffinservice.boxservice.user.service;

import com.tiffinservice.boxservice.user.dto.AddressRequestDTO;
import com.tiffinservice.boxservice.user.dto.AddressResponseDTO;

import java.util.List;

public interface AddressService {
    AddressResponseDTO createAddress(AddressRequestDTO dto);
    AddressResponseDTO getAddressById(Long id);
    List<AddressResponseDTO> getAddressesByUserId(Long userId);
    AddressResponseDTO updateAddress(Long id, AddressRequestDTO dto);
    void deleteAddress(Long id);
}
