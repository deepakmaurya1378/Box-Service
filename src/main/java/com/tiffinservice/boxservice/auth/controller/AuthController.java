package com.tiffinservice.boxservice.auth.controller;

import com.tiffinservice.boxservice.auth.dto.*;
import com.tiffinservice.boxservice.auth.enums.AppRole;
import com.tiffinservice.boxservice.auth.service.AuthService;
import com.tiffinservice.boxservice.common.exception.BadRequestException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO dto, HttpServletResponse response) {

        AuthResultDTO result = authService.login(dto);

        Cookie refreshCookie = new Cookie("refresh_token", result.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false); // true in production
        refreshCookie.setPath("/auth");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(refreshCookie);

        return new AuthResponseDTO(
                result.getAccessToken(),
                result.getUserId(),
                result.getRole()
        );
    }


    @PostMapping("/user/otp/request")
    public void requestOtp(@RequestBody OtpRequestDTO dto) {
        authService.requestOtp(dto);
    }

    @PostMapping("/user/otp/verify")
    public AuthResponseDTO verifyOtp(@RequestBody OtpVerifyDTO dto, HttpServletResponse response) {

        AuthResultDTO result = authService.verifyOtp(dto);

        Cookie refreshCookie = new Cookie("refresh_token", result.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false); // true in production
        refreshCookie.setPath("/auth");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(refreshCookie);

        return new AuthResponseDTO(result.getAccessToken(), result.getUserId(), result.getRole());
    }


    @PostMapping("/refresh")
    public AuthResponseDTO refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken) {

        if (refreshToken == null) {throw new BadRequestException("Refresh token missing");}

        AuthResultDTO result = authService.refresh(refreshToken);

        return new AuthResponseDTO(result.getAccessToken(), result.getUserId(), result.getRole());
    }

    @PostMapping("/logout")
    public void logout(@CookieValue(name = "refresh_token", required = false) String refreshToken, HttpServletResponse response) {

        if (refreshToken != null) {authService.logout(refreshToken);}

        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true in production
        cookie.setPath("/auth");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @PostMapping("/admin/force-logout/{userId}")
    public void forceLogout(@PathVariable Long userId, @RequestParam AppRole role) {
        authService.forceLogout(userId, role);
    }
}
