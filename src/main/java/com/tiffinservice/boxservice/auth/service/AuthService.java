package com.tiffinservice.boxservice.auth.service;

import com.tiffinservice.boxservice.admin.repository.AdminRepository;
import com.tiffinservice.boxservice.auth.dto.*;
import com.tiffinservice.boxservice.auth.entity.*;
import com.tiffinservice.boxservice.auth.enums.AppRole;
import com.tiffinservice.boxservice.auth.repository.*;
import com.tiffinservice.boxservice.auth.security.CustomUserDetails;
import com.tiffinservice.boxservice.auth.util.JwtUtil;
import com.tiffinservice.boxservice.common.exception.BadRequestException;
import com.tiffinservice.boxservice.user.repository.UserRepository;
import com.tiffinservice.boxservice.vendor.repository.VendorRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminRepository adminRepo;
    private final VendorRepository vendorRepo;
    private final UserRepository userRepo;
    private final SessionRepository sessionRepo;
    private final RefreshTokenRepository refreshRepo;
    private final OtpService otpService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;


    public AuthResultDTO login(LoginRequestDTO dto) {

        CustomUserDetails principal;

        switch (dto.getRole()) {

            case ADMIN -> {

                var admin = adminRepo.findByEmail(dto.getIdentifier())
                        .orElseThrow(() ->
                                new BadCredentialsException("Invalid email or password")
                        );

                System.out.println("========== ADMIN LOGIN DEBUG ==========");
                System.out.println("EMAIL FROM REQUEST: " + dto.getIdentifier());
                System.out.println("RAW PASSWORD FROM REQUEST: " + dto.getPassword());
                System.out.println("HASH FROM DB: " + admin.getPasswordHash());
                System.out.println("PASSWORD MATCHES: " +
                        passwordEncoder.matches(dto.getPassword(), admin.getPasswordHash()));
                System.out.println("ADMIN ACTIVE: " + admin.getIsActive());
                System.out.println("======================================");

                if (!passwordEncoder.matches(dto.getPassword(), admin.getPasswordHash())) {
                    throw new BadCredentialsException("Invalid email or password");
                }

                if (!Boolean.TRUE.equals(admin.getIsActive())) {
                    throw new AccessDeniedException("Admin account is disabled");
                }

                principal = new CustomUserDetails(
                        admin.getId(),
                        admin.getEmail(),
                        admin.getPasswordHash(),
                        AppRole.ADMIN,
                        admin.getIsActive()
                );
            }


            case VENDOR -> {
                var vendor = vendorRepo.findByEmailOrPhone(dto.getIdentifier(), dto.getIdentifier())
                        .orElseThrow(() -> new BadCredentialsException("Invalid email/phone or password"));

                if (!passwordEncoder.matches(dto.getPassword(), vendor.getPasswordHash())) {
                    throw new BadCredentialsException("Invalid email/phone or password");
                }

                if (!Boolean.TRUE.equals(vendor.getIsActive())) {
                    throw new AccessDeniedException("Vendor account is disabled");
                }

                principal = new CustomUserDetails(
                        vendor.getId(),
                        vendor.getEmail(),
                        vendor.getPasswordHash(),
                        AppRole.VENDOR,
                        vendor.getIsActive()
                );
            }

            case USER -> {

                var user = userRepo.findByEmailOrPhone(dto.getIdentifier(), dto.getIdentifier())
                        .orElseThrow(() -> new BadCredentialsException("Invalid email/phone or password"));

                if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
                    throw new BadCredentialsException("Invalid email/phone or password");
                }

                if (!Boolean.TRUE.equals(user.getIsActive()) || Boolean.TRUE.equals(user.getIsBlocked())) {
                    throw new AccessDeniedException("User account is disabled");
                }

                principal = new CustomUserDetails(
                        user.getId(),
                        user.getEmail(),
                        user.getPasswordHash(),
                        AppRole.USER,
                        user.getIsActive()
                );
            }

            default -> throw new BadRequestException("Invalid role");
        }

        return issueTokens(principal, dto.getDeviceId());
    }


    public void requestOtp(OtpRequestDTO dto) {
        otpService.sendOtp(dto.getIdentifier());
    }

    public AuthResultDTO verifyOtp(OtpVerifyDTO dto) {
        CustomUserDetails user = otpService.verify(dto.getIdentifier(), dto.getOtp());
        return issueTokens(user, dto.getDeviceId());
    }


    private AuthResultDTO issueTokens(CustomUserDetails user, String deviceId) {

        Session session = sessionRepo.save(
                Session.builder()
                        .userId(user.getId())
                        .role(user.getRole())
                        .deviceId(deviceId)
                        .active(true)
                        .expiresAt(LocalDateTime.now().plusDays(7))
                        .build()
        );

        String refreshToken = jwtUtil.generateRefreshToken(
                user.getId(),
                user.getRole(),
                session.getId()
        );

        refreshRepo.save(
                RefreshToken.builder()
                        .token(refreshToken)
                        .userId(user.getId())
                        .role(user.getRole())
                        .sessionId(session.getId())
                        .revoked(false)
                        .expiresAt(LocalDateTime.now().plusDays(7))
                        .build()
        );

        String accessToken =
                jwtUtil.generateAccessToken(
                        user.getUsername(),
                        user.getId(),
                        user.getRole()
                );

        return new AuthResultDTO(
                accessToken,
                refreshToken,
                user.getId(),
                user.getRole()
        );
    }


    public AuthResultDTO refresh(String token) {

        RefreshToken refresh =
                refreshRepo.findByToken(token)
                        .orElseThrow(() ->
                                new BadRequestException("Invalid or expired refresh token")
                        );

        if (Boolean.TRUE.equals(refresh.getRevoked())
                || refresh.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Refresh token expired. Please login again.");
        }

        Session session =
                sessionRepo.findById(refresh.getSessionId())
                        .orElseThrow(() ->
                                new BadRequestException("Session expired. Please login again.")
                        );

        if (!Boolean.TRUE.equals(session.getActive())) {
            throw new AccessDeniedException("Session expired. Please login again.");
        }

        String accessToken =
                jwtUtil.generateAccessToken(
                        null,
                        refresh.getUserId(),
                        refresh.getRole()
                );

        return new AuthResultDTO(
                accessToken,
                refresh.getToken(),
                refresh.getUserId(),
                refresh.getRole()
        );
    }


    public void logout(String token) {

        RefreshToken refresh =
                refreshRepo.findByToken(token)
                        .orElseThrow(() ->
                                new BadRequestException("Invalid refresh token")
                        );

        refresh.setRevoked(true);
        refreshRepo.save(refresh);

        sessionRepo.findById(refresh.getSessionId())
                .ifPresent(session -> {
                    session.setActive(false);
                    sessionRepo.save(session);
                });
    }


    public void forceLogout(Long userId, AppRole role) {

        sessionRepo.findByUserIdAndRoleAndActiveTrue(userId, role)
                .forEach(session -> {
                    session.setActive(false);
                    sessionRepo.save(session);

                    refreshRepo.findAll().stream()
                            .filter(rt ->
                                    rt.getSessionId().equals(session.getId())
                                            && !Boolean.TRUE.equals(rt.getRevoked())
                            )
                            .forEach(rt -> {
                                rt.setRevoked(true);
                                refreshRepo.save(rt);
                            });
                });
    }
}
