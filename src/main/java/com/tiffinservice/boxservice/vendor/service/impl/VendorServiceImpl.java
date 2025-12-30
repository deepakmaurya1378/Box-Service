package com.tiffinservice.boxservice.vendor.service.impl;

import com.tiffinservice.boxservice.common.enums.DocumentVerificationStatus;
import com.tiffinservice.boxservice.common.enums.MealType;
import com.tiffinservice.boxservice.common.enums.VendorStatus;
import com.tiffinservice.boxservice.common.enums.VendorType;
import com.tiffinservice.boxservice.common.exception.BadRequestException;
import com.tiffinservice.boxservice.common.exception.ConflictException;
import com.tiffinservice.boxservice.common.exception.ResourceNotFoundException;
import com.tiffinservice.boxservice.common.utils.GeoUtils;
import com.tiffinservice.boxservice.user.entity.Address;
import com.tiffinservice.boxservice.user.repository.AddressRepository;
import com.tiffinservice.boxservice.vendor.dto.NearbyVendorResponseDTO;
import com.tiffinservice.boxservice.vendor.dto.VendorRequestDTO;
import com.tiffinservice.boxservice.vendor.dto.VendorResponseDTO;
import com.tiffinservice.boxservice.vendor.dto.VendorUpdateDTO;
import com.tiffinservice.boxservice.vendor.entity.Vendor;
import com.tiffinservice.boxservice.vendor.mapper.VendorMapper;
import com.tiffinservice.boxservice.vendor.repository.VendorRepository;
import com.tiffinservice.boxservice.vendor.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public VendorResponseDTO RegisterVendor(VendorRequestDTO dto) {

        if (dto.getEmail() == null || dto.getEmail().isBlank())
            throw new BadRequestException("Email is required");

        if (dto.getPhone() == null || dto.getPhone().isBlank())
            throw new BadRequestException("Phone is required");

        if (dto.getVendorName() == null || dto.getVendorName().isBlank())
            throw new BadRequestException("Vendor name is required");

        if (dto.getOwnerName() == null || dto.getOwnerName().isBlank())
            throw new BadRequestException("Owner name is required");

        if (dto.getCity() == null || dto.getCity().isBlank())
            throw new BadRequestException("City is required");

        if (dto.getLatitude() == null || dto.getLongitude() == null)
            throw new BadRequestException("Latitude and Longitude are required");

        if (dto.getDeliveryRadiusKm() == null ||
                dto.getDeliveryRadiusKm().compareTo(BigDecimal.ZERO) <= 0)
            throw new BadRequestException("Delivery radius must be greater than zero");

        if (dto.getPasswordHash() == null || dto.getPasswordHash().isBlank())
            throw new BadRequestException("Password is required");

        if (dto.getCommissionRate() == null)
            throw new BadRequestException("Commission rate is mandatory");

        if (vendorRepository.existsByEmailOrPhone(dto.getEmail(), dto.getPhone()))
            throw new ConflictException("Vendor already exists");

        if (dto.getAdharCard() == null || dto.getAdharCard().isBlank())
            throw new BadRequestException("Aadhaar card is required");

        if (dto.getPanCard() == null || dto.getPanCard().isBlank())
            throw new BadRequestException("PAN card is required");

        if (dto.getVendorType() == VendorType.LICENSED) {
            if (dto.getFssaiLicense() == null || dto.getFssaiLicense().isBlank()
                    || dto.getFssaiLicenseNumber() == null || dto.getFssaiLicenseNumber().isBlank()) {
                throw new BadRequestException("FSSAI license details are required for licensed vendors");
            }
        }



        Vendor vendor = vendorMapper.toEntity(dto, new Vendor());

        vendor.setPasswordHash(passwordEncoder.encode(dto.getPasswordHash()));
        vendor.setStatus(VendorStatus.PENDING);
        vendor.setDocumentVerificationStatus(DocumentVerificationStatus.PENDING);
        vendor.setVerificationNotes(null);
        vendor.setIsActive(true);
        vendor.setAverageRating(BigDecimal.ZERO);

        return vendorMapper.toDTO(vendorRepository.save(vendor));
    }

    @Override
    public VendorResponseDTO getVendorById(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + id));
        return vendorMapper.toDTO(vendor);
    }

    @Override
    public VendorResponseDTO updateVendor(Long id, VendorUpdateDTO dto) {

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + id));

        if ((dto.getLatitude() != null && dto.getLongitude() == null) ||
                (dto.getLatitude() == null && dto.getLongitude() != null)) {
            throw new BadRequestException("Both latitude and longitude must be provided together");
        }

        if (dto.getDeliveryRadiusKm() != null &&
                dto.getDeliveryRadiusKm().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Delivery radius must be greater than zero");
        }

        if (dto.getAvailableMeals() != null && dto.getAvailableMeals().isEmpty()) {
            throw new BadRequestException("Available meals cannot be empty");
        }

        if ((dto.getBreakfastStartTime() != null && dto.getBreakfastEndTime() == null) ||
                (dto.getBreakfastStartTime() == null && dto.getBreakfastEndTime() != null)) {
            throw new BadRequestException("Both breakfast start and end time are required");
        }

        if ((dto.getLunchStartTime() != null && dto.getLunchEndTime() == null) ||
                (dto.getLunchStartTime() == null && dto.getLunchEndTime() != null)) {
            throw new BadRequestException("Both lunch start and end time are required");
        }

        if ((dto.getDinnerStartTime() != null && dto.getDinnerEndTime() == null) ||
                (dto.getDinnerStartTime() == null && dto.getDinnerEndTime() != null)) {
            throw new BadRequestException("Both dinner start and end time are required");
        }

        if (dto.getVendorName() != null) vendor.setVendorName(dto.getVendorName());
        if (dto.getOwnerName() != null) vendor.setOwnerName(dto.getOwnerName());
        if (dto.getPhone() != null) vendor.setPhone(dto.getPhone());

        if (dto.getSellingUnderCompanyName() != null)
            vendor.setSellingUnderCompanyName(dto.getSellingUnderCompanyName());

        if (dto.getAddressLine1() != null) vendor.setAddressLine1(dto.getAddressLine1());
        if (dto.getAddressLine2() != null) vendor.setAddressLine2(dto.getAddressLine2());
        if (dto.getCity() != null) vendor.setCity(dto.getCity());
        if (dto.getState() != null) vendor.setState(dto.getState());
        if (dto.getPincode() != null) vendor.setPincode(dto.getPincode());

        if (dto.getLatitude() != null) vendor.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) vendor.setLongitude(dto.getLongitude());
        if (dto.getDeliveryRadiusKm() != null)
            vendor.setDeliveryRadiusKm(dto.getDeliveryRadiusKm());

        if (dto.getAvailableMeals() != null)
            vendor.setAvailableMeals(dto.getAvailableMeals());

        if (dto.getBreakfastStartTime() != null)
            vendor.setBreakfastStartTime(dto.getBreakfastStartTime());
        if (dto.getBreakfastEndTime() != null)
            vendor.setBreakfastEndTime(dto.getBreakfastEndTime());

        if (dto.getLunchStartTime() != null)
            vendor.setLunchStartTime(dto.getLunchStartTime());
        if (dto.getLunchEndTime() != null)
            vendor.setLunchEndTime(dto.getLunchEndTime());

        if (dto.getDinnerStartTime() != null)
            vendor.setDinnerStartTime(dto.getDinnerStartTime());
        if (dto.getDinnerEndTime() != null)
            vendor.setDinnerEndTime(dto.getDinnerEndTime());

        return vendorMapper.toDTO(vendorRepository.save(vendor));
    }


    @Override
    public void deleteVendor(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + id));
        vendorRepository.delete(vendor);
    }

    @Override
    public List<NearbyVendorResponseDTO> getNearbyDeliverableVendors(Long addressId) {

        Address address = addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        double userLat = address.getLatitude().doubleValue();
        double userLon = address.getLongitude().doubleValue();

        return vendorRepository.findByCityAndIsActiveTrue(address.getCity())
                .stream()
                .filter(v -> v.getStatus() == VendorStatus.APPROVED)
                .filter(v -> v.getLatitude() != null && v.getLongitude() != null)
                .map(v -> new Object[]{
                        v,
                        GeoUtils.distanceKm(userLat, userLon,
                                v.getLatitude().doubleValue(),
                                v.getLongitude().doubleValue())
                })
                .filter(arr -> BigDecimal.valueOf((double) arr[1]).compareTo(((Vendor) arr[0]).getDeliveryRadiusKm()) <= 0
                )
                .sorted(Comparator.comparingDouble(arr -> (double) arr[1]))
                .map(arr -> {
                    Vendor v = (Vendor) arr[0];
                    return NearbyVendorResponseDTO.builder()
                            .vendorId(v.getId())
                            .vendorName(v.getVendorName())
                            .city(v.getCity())
                            .distanceKm(BigDecimal.valueOf((double) arr[1]))
                            .deliveryRadiusKm(v.getDeliveryRadiusKm())
                            .averageRating(v.getAverageRating())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<NearbyVendorResponseDTO> getNearbyDeliverableVendors(Long addressId, MealType mealType) {

        Address address = addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        double userLat = address.getLatitude().doubleValue();
        double userLon = address.getLongitude().doubleValue();

        return vendorRepository.findByCityAndIsActiveTrue(address.getCity())
                .stream()
                .filter(v -> v.getStatus() == VendorStatus.APPROVED)
                .filter(v -> v.getAvailableMeals() != null && v.getAvailableMeals().contains(mealType))
                .filter(v -> v.getLatitude() != null && v.getLongitude() != null)
                .map(v -> new Object[]{
                        v,
                        GeoUtils.distanceKm(userLat, userLon, v.getLatitude().doubleValue(), v.getLongitude().doubleValue())
                })
                .filter(arr -> BigDecimal.valueOf((double) arr[1]).compareTo(((Vendor) arr[0]).getDeliveryRadiusKm()) <= 0)
                .sorted(Comparator.comparingDouble(arr -> (double) arr[1]))
                .map(arr -> {
                    Vendor v = (Vendor) arr[0];
                    return NearbyVendorResponseDTO.builder()
                            .vendorId(v.getId())
                            .vendorName(v.getVendorName())
                            .city(v.getCity())
                            .distanceKm(BigDecimal.valueOf((double) arr[1]))
                            .deliveryRadiusKm(v.getDeliveryRadiusKm())
                            .averageRating(v.getAverageRating())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
