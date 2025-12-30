package com.tiffinservice.boxservice.user.controller;

import com.tiffinservice.boxservice.user.dto.UserCreateRequestDTO;
import com.tiffinservice.boxservice.user.dto.UserCreateResponseDTO;
import com.tiffinservice.boxservice.user.dto.UserUpdateDTO;
import com.tiffinservice.boxservice.user.service.UserService;
import com.tiffinservice.boxservice.common.security.SecurityUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserCreateResponseDTO> register(
            @Valid @RequestBody UserCreateRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.register(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserCreateResponseDTO> getMe() {
        Long userId = SecurityUtils.getUserId();
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserCreateResponseDTO> updateMe(
            @RequestBody UserUpdateDTO request
    ) {
        Long userId = SecurityUtils.getUserId();
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe() {
        Long userId = SecurityUtils.getUserId();
        userService.softDeleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
