package com.tiffinservice.boxservice.auth.service;

import com.tiffinservice.boxservice.auth.entity.OtpToken;
import com.tiffinservice.boxservice.auth.repository.OtpTokenRepository;
import com.tiffinservice.boxservice.auth.security.CustomUserDetails;
import com.tiffinservice.boxservice.auth.enums.AppRole;
import com.tiffinservice.boxservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpTokenRepository otpRepo;
    private final UserRepository userRepo;

    public void sendOtp(String identifier) {

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        otpRepo.save(OtpToken.builder()
                        .identifier(identifier)
                        .otp(otp)
                        .expiresAt(LocalDateTime.now().plusMinutes(5))
                        .build()
        );


        // i still have integrate SMS / Email gateway here



    }

    public CustomUserDetails verify(String identifier, String otp) {

        OtpToken token = otpRepo.findTopByIdentifierAndUsedFalseOrderByExpiresAtDesc(identifier).orElseThrow(() -> new RuntimeException("OTP expired"));

        if (!token.getOtp().equals(otp)) {throw new RuntimeException("Invalid OTP");}
        token.setUsed(true);
        otpRepo.save(token);

        var user = userRepo.findByEmailOrPhone(identifier, identifier).orElseThrow();

        return new CustomUserDetails(user.getId(), user.getEmail(), null, AppRole.USER, true);
    }
}
