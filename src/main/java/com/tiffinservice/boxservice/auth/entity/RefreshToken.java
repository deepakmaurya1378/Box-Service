package com.tiffinservice.boxservice.auth.entity;

import com.tiffinservice.boxservice.auth.enums.AppRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private AppRole role;

    private Long sessionId;

    private Boolean revoked = false;

    private LocalDateTime expiresAt;

    private LocalDateTime createdAt = LocalDateTime.now();
}
