package com.tiffinservice.boxservice.auth.repository;

import com.tiffinservice.boxservice.auth.entity.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findTopByIdentifierAndUsedFalseOrderByExpiresAtDesc(String identifier);
}
