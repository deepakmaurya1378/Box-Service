package com.tiffinservice.boxservice.admin.controller;

import com.tiffinservice.boxservice.admin.dto.*;
import com.tiffinservice.boxservice.admin.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserManagementController {

    private final UserManagementService userManagementService;

    @GetMapping
    public List<AdminUserResponseDTO> listUsers(@RequestParam(required = false) Boolean isActive, @RequestParam(required = false) Boolean isBlocked) {
        return userManagementService.listUsers(isActive, isBlocked);
    }

    @GetMapping("/{userId}")
    public AdminUserResponseDTO getUserDetails(@PathVariable Long userId) {
        return userManagementService.getUserDetails(userId);
    }

    @PatchMapping("/{userId}/block")
    public void blockUser(@PathVariable Long userId) {
        userManagementService.blockUser(userId);
    }

    @PatchMapping("/{userId}/unblock")
    public void unblockUser(@PathVariable Long userId) {
        userManagementService.unblockUser(userId);
    }

    @PatchMapping("/{userId}/deactivate")
    public void deactivateUser(@PathVariable Long userId) {
        userManagementService.deactivateUser(userId);
    }

    @GetMapping("/{userId}/addresses")
    public List<UserAddressDTO> getUserAddresses(@PathVariable Long userId) {
        return userManagementService.getUserAddresses(userId);
    }

    @GetMapping("/{userId}/preferences")
    public List<UserPreferenceDTO> getUserPreferences(@PathVariable Long userId) {
        return userManagementService.getUserPreferences(userId);
    }

    @GetMapping("/{userId}/wallet")
    public UserWalletDTO getUserWallet(@PathVariable Long userId) {
        return userManagementService.getUserWallet(userId);
    }
}
