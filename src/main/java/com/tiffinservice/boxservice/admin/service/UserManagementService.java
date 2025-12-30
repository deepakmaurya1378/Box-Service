package com.tiffinservice.boxservice.admin.service;

import com.tiffinservice.boxservice.admin.dto.*;

import java.util.List;

public interface UserManagementService {

    List<AdminUserResponseDTO> listUsers(Boolean isActive, Boolean isBlocked);
    AdminUserResponseDTO getUserDetails(Long userId);

    void blockUser(Long userId);
    void unblockUser(Long userId);
    void deactivateUser(Long userId);

    List<UserAddressDTO> getUserAddresses(Long userId);

    List<UserPreferenceDTO> getUserPreferences(Long userId);

    UserWalletDTO getUserWallet(Long userId);
}
