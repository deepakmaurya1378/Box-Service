package com.tiffinservice.boxservice.user.service.impl;

import com.tiffinservice.boxservice.common.enums.AddressType;
import com.tiffinservice.boxservice.common.enums.PreferenceType;
import com.tiffinservice.boxservice.common.enums.ShiftType;
import com.tiffinservice.boxservice.common.exception.BadRequestException;
import com.tiffinservice.boxservice.common.exception.ResourceNotFoundException;
import com.tiffinservice.boxservice.user.dto.AddressRequestDTO;
import com.tiffinservice.boxservice.user.dto.AddressResponseDTO;
import com.tiffinservice.boxservice.user.entity.Address;
import com.tiffinservice.boxservice.user.entity.User;
import com.tiffinservice.boxservice.user.mapper.AddressMapper;
import com.tiffinservice.boxservice.user.repository.AddressRepository;
import com.tiffinservice.boxservice.user.repository.UserRepository;
import com.tiffinservice.boxservice.user.repository.UserVendorPreferenceRepository;
import com.tiffinservice.boxservice.user.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final UserVendorPreferenceRepository preferenceRepository;
    private final AddressMapper addressMapper;

    @Override
    public AddressResponseDTO createAddress(AddressRequestDTO dto) {

        if (addressRepository.existsByUser_IdAndAddressType(
                dto.getUserId(),
                dto.getAddressType()
        )) {
            throw new BadRequestException(
                    "Address already exists for " + dto.getAddressType() +
                            ". Please update the existing address."
            );
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Address address = addressMapper.toEntity(dto, user);
        return addressMapper.toDto(addressRepository.save(address));
    }

    @Override
    public AddressResponseDTO getAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found"));
        return addressMapper.toDto(address);
    }

    @Override
    public List<AddressResponseDTO> getAddressesByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return addressRepository.findByUser(user)
                .stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AddressResponseDTO updateAddress(Long id, AddressRequestDTO dto) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found"));

        if (dto.getAddressLine1() != null) address.setAddressLine1(dto.getAddressLine1());
        if (dto.getAddressLine2() != null) address.setAddressLine2(dto.getAddressLine2());
        if (dto.getLandmark() != null) address.setLandmark(dto.getLandmark());
        if (dto.getCity() != null) address.setCity(dto.getCity());
        if (dto.getState() != null) address.setState(dto.getState());
        if (dto.getCountry() != null) address.setCountry(dto.getCountry());
        if (dto.getPostalCode() != null) address.setPostalCode(dto.getPostalCode());
        if (dto.getLatitude() != null) address.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) address.setLongitude(dto.getLongitude());

        return addressMapper.toDto(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(Long id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found"));

        ShiftType shiftType =
                ShiftType.valueOf(address.getAddressType().name());

        boolean defaultVendorExists =
                preferenceRepository
                        .existsByUserIdAndShiftTypeAndPreferenceType(
                                address.getUser().getId(),
                                shiftType,
                                PreferenceType.DEFAULT
                        );

        if (defaultVendorExists) {
            throw new BadRequestException(
                    "Default vendor exists for " + shiftType +
                            ".First remove all the prefered vendor for" + shiftType +". Then delete this."
            );
        }

        addressRepository.delete(address);
    }
}
