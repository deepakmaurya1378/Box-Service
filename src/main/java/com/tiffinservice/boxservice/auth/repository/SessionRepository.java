package com.tiffinservice.boxservice.auth.repository;

import com.tiffinservice.boxservice.auth.entity.Session;
import com.tiffinservice.boxservice.auth.enums.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByUserIdAndRoleAndActiveTrue(Long userId, AppRole role);
}
