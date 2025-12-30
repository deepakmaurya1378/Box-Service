package com.tiffinservice.boxservice.auth.repository;

import com.tiffinservice.boxservice.auth.entity.RefreshToken;
import com.tiffinservice.boxservice.auth.enums.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    List<RefreshToken> findByUserIdAndRoleAndRevokedFalse(Long userId, AppRole role);
}
