package com.tiffinservice.boxservice.admin.service.impl;

import com.tiffinservice.boxservice.admin.dto.*;
import com.tiffinservice.boxservice.admin.service.UserManagementService;
import com.tiffinservice.boxservice.common.exception.ResourceNotFoundException;
import com.tiffinservice.boxservice.user.entity.*;
import com.tiffinservice.boxservice.user.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final UserVendorPreferenceRepository preferenceRepository;
    private final WalletRepository walletRepository;

    @Override
    public List<AdminUserResponseDTO> listUsers(Boolean isActive, Boolean isBlocked) {

        return userRepository.findAll()
                .stream()
                .filter(u -> isActive == null || u.getIsActive().equals(isActive))
                .filter(u -> isBlocked == null || u.getIsBlocked().equals(isBlocked))
                .map(this::mapToAdminUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdminUserResponseDTO getUserDetails(Long userId) {
        User user = getUser(userId);
        return mapToAdminUserDTO(user);
    }

    @Override
    public void blockUser(Long userId) {
        User user = getUser(userId);
        user.setIsBlocked(true);
        userRepository.save(user);
    }

    @Override
    public void unblockUser(Long userId) {
        User user = getUser(userId);
        user.setIsBlocked(false);
        userRepository.save(user);
    }

    @Override
    public void deactivateUser(Long userId) {
        User user = getUser(userId);
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    public List<UserAddressDTO> getUserAddresses(Long userId) {
        User user = getUser(userId);

        return addressRepository.findByUser(user)
                .stream()
                .map(UserAddressDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserPreferenceDTO> getUserPreferences(Long userId) {

        return preferenceRepository.findAll()
                .stream()
                .filter(p -> p.getUser().getId().equals(userId))
                .map(UserPreferenceDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public UserWalletDTO getUserWallet(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        return UserWalletDTO.from(wallet);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId)
                );
    }

    private AdminUserResponseDTO mapToAdminUserDTO(User user) {
        return AdminUserResponseDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .isActive(user.getIsActive())
                .isBlocked(user.getIsBlocked())
                .isEmailVerified(user.getIsEmailVerified())
                .isPhoneVerified(user.getIsPhoneVerified())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
