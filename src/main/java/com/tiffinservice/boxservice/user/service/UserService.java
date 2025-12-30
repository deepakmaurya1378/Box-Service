package com.tiffinservice.boxservice.user.service;

import com.tiffinservice.boxservice.user.dto.UserCreateRequestDTO;
import com.tiffinservice.boxservice.user.dto.UserCreateResponseDTO;
import com.tiffinservice.boxservice.user.dto.UserUpdateDTO;

public interface UserService {

    UserCreateResponseDTO register(UserCreateRequestDTO dto);

    UserCreateResponseDTO getUserById(Long id);

    UserCreateResponseDTO updateUser(Long id, UserUpdateDTO dto);

    void softDeleteUser(Long id);
}
