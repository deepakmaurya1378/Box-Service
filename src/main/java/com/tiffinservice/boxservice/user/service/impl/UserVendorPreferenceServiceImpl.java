package com.tiffinservice.boxservice.user.service.impl;

import com.tiffinservice.boxservice.common.enums.AddressType;
import com.tiffinservice.boxservice.common.enums.PreferenceType;
import com.tiffinservice.boxservice.common.enums.ShiftType;
import com.tiffinservice.boxservice.common.exception.BadRequestException;
import com.tiffinservice.boxservice.common.exception.ConflictException;
import com.tiffinservice.boxservice.common.exception.ResourceNotFoundException;
import com.tiffinservice.boxservice.common.utils.GeoUtils;
import com.tiffinservice.boxservice.user.dto.UserVendorPreferenceRequestDTO;
import com.tiffinservice.boxservice.user.dto.UserVendorPreferenceResponseDTO;
import com.tiffinservice.boxservice.user.entity.Address;
import com.tiffinservice.boxservice.user.entity.User;
import com.tiffinservice.boxservice.user.entity.UserVendorPreference;
import com.tiffinservice.boxservice.user.mapper.UserVendorPreferenceMapper;
import com.tiffinservice.boxservice.user.repository.AddressRepository;
import com.tiffinservice.boxservice.user.repository.UserVendorPreferenceRepository;
import com.tiffinservice.boxservice.vendor.entity.Vendor;
import com.tiffinservice.boxservice.user.service.UserVendorPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserVendorPreferenceServiceImpl implements UserVendorPreferenceService {

    private final UserVendorPreferenceRepository repo;
    private final AddressRepository addressRepository;

    @Override
    public UserVendorPreferenceResponseDTO addVendorPreference(
            UserVendorPreferenceRequestDTO req
    ) {

        int count = repo.countByUserIdAndShiftType(
                req.getUserId(),
                req.getShiftType()
        );

        if (count >= 5) {
            throw new ConflictException(
                    "You already have 5 preferred vendors for " + req.getShiftType()
            );
        }

        if (req.getPreferenceType() == PreferenceType.DEFAULT || req.getPreferenceType() == PreferenceType.PREFERRED ) {

            Address address = addressRepository.findByUser_IdAndAddressType(req.getUserId(), AddressType.valueOf(req.getShiftType().name())).orElseThrow(() ->
                            new BadRequestException(
                                    "Please add address for " + req.getShiftType() +
                                            " before setting Prefered vendor"
                            )
                    );

            Vendor vendor = Vendor.builder().id(req.getVendorId()).build();

            double distanceKm = GeoUtils.distanceKm(address.getLatitude().doubleValue(), address.getLongitude().doubleValue(), vendor.getLatitude().doubleValue(), vendor.getLongitude().doubleValue());

            if (BigDecimal.valueOf(distanceKm).compareTo(vendor.getDeliveryRadiusKm()) > 0) {

                throw new BadRequestException(
                        "Vendor cannot deliver to your " + req.getShiftType() + " address");
            }

            boolean defaultExists =
                    repo.existsByUserIdAndShiftTypeAndPreferenceType(req.getUserId(), req.getShiftType(), PreferenceType.DEFAULT);

            if (defaultExists) {
                throw new ConflictException( "Default vendor already exists for " + req.getShiftType());
            }
        }

        UserVendorPreference pref = UserVendorPreference.builder()
                .user(User.builder().id(req.getUserId()).build())
                .vendor(Vendor.builder().id(req.getVendorId()).build())
                .shiftType(req.getShiftType())
                .preferenceType(req.getPreferenceType())
                .build();

        return UserVendorPreferenceMapper.toDTO(repo.save(pref));
    }

    @Override
    public List<UserVendorPreferenceResponseDTO> getPreferencesByShift(Long userId, ShiftType shiftType) {
        List<UserVendorPreference> list = repo.findByUserIdAndShiftType(userId, shiftType);
        if (list.isEmpty()) { throw new ResourceNotFoundException("No vendors found for " + shiftType + " shift."); }
        return list.stream().map(UserVendorPreferenceMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public void removeVendorPreference(Long userId, Long vendorId, ShiftType shiftType) {
        UserVendorPreference pref = repo.findByUserIdAndVendorIdAndShiftType(userId, vendorId, shiftType)
                .orElseThrow(() -> new ResourceNotFoundException( "This vendor is not present in your " + shiftType + " preference list."));

        if (pref.getPreferenceType() == PreferenceType.DEFAULT) {
            throw new BadRequestException("You cannot remove your default vendor for " + shiftType);
        }
        repo.delete(pref);
    }

    @Override
    public UserVendorPreferenceResponseDTO changeDefaultVendor(Long userId, Long vendorId, ShiftType shiftType) {

        List<UserVendorPreference> list = repo.findByUserIdAndShiftType(userId, shiftType);
        boolean exists =  list.stream().anyMatch(x -> x.getVendor().getId().equals(vendorId));

        if (!exists) { throw new ResourceNotFoundException( "Vendor does not exist in your preference list for " + shiftType ); }

        list.forEach(pref -> {
            if (pref.getPreferenceType() == PreferenceType.DEFAULT) {
                pref.setPreferenceType(PreferenceType.PREFERRED);
                repo.save(pref);
            }
        });

        UserVendorPreference newDefault =
                list.stream()
                        .filter(x -> x.getVendor().getId().equals(vendorId))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        newDefault.setPreferenceType(PreferenceType.DEFAULT);
        repo.save(newDefault);
        return UserVendorPreferenceMapper.toDTO(newDefault);
    }

    @Override
    public UserVendorPreferenceResponseDTO removeDefaultVendor(Long userId, Long vendorId, ShiftType shiftType) {
        UserVendorPreference preference =
                repo.findByUserIdAndVendorIdAndShiftType(userId, vendorId, shiftType).orElseThrow(() ->
                        new ResourceNotFoundException("Vendor not found in your preference list for " + shiftType));

        if (preference.getPreferenceType() != PreferenceType.DEFAULT) { throw new IllegalStateException("This vendor is not Default."); }
        preference.setPreferenceType(PreferenceType.PREFERRED);
        repo.save(preference);
        return UserVendorPreferenceMapper.toDTO(preference);
    }
}
