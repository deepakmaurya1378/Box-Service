package com.tiffinservice.boxservice.user.service.impl;

import com.tiffinservice.boxservice.user.dto.UserCreateRequestDTO;
import com.tiffinservice.boxservice.user.dto.UserCreateResponseDTO;
import com.tiffinservice.boxservice.user.dto.UserUpdateDTO;
import com.tiffinservice.boxservice.user.entity.User;
import com.tiffinservice.boxservice.user.repository.UserRepository;
import com.tiffinservice.boxservice.user.service.UserService;
import com.tiffinservice.boxservice.user.mapper.UserMapper;
import com.tiffinservice.boxservice.common.exception.BadRequestException;
import com.tiffinservice.boxservice.common.exception.ResourceNotFoundException;
import com.tiffinservice.boxservice.common.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    @Override
    public UserCreateResponseDTO register(UserCreateRequestDTO dto) {

        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new BadRequestException("Password is required");
        }

        if (dto.getEmail() != null && userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("Email already in use");
        }

        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new ConflictException("Phone already in use");
        }

        User user = mapper.toEntity(dto);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setIsActive(true);
        user.setIsBlocked(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return mapper.toDto(userRepository.save(user));
    }

    @Override
    public UserCreateResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", id));

        if (!user.getIsActive() || user.getIsBlocked()) {
            throw new BadRequestException("User account is inactive or blocked");
        }

        return mapper.toDto(user);
    }

    @Override
    public UserCreateResponseDTO updateUser(Long id, UserUpdateDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", id));

        if (dto.getFullName() != null) user.setFullName(dto.getFullName());
        if (dto.getProfileImageUrl() != null) user.setProfileImageUrl(dto.getProfileImageUrl());
        if (dto.getDob() != null) user.setDob(dto.getDob());
        if (dto.getGender() != null) user.setGender(dto.getGender());

        user.setUpdatedAt(LocalDateTime.now());
        return mapper.toDto(userRepository.save(user));
    }

    @Override
    public void softDeleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", id));

        if (!user.getIsActive()) {
            throw new BadRequestException("User already inactive");
        }

        user.setIsActive(false);
        user.setIsBlocked(true);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }
}
