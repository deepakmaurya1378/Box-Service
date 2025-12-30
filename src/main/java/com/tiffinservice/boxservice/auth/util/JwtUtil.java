package com.tiffinservice.boxservice.auth.util;

import com.tiffinservice.boxservice.auth.enums.AppRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "very-secure-secret-key-for-production-usage-only-should-be-256-bit";

    private static final long ACCESS_TOKEN_EXPIRY = 15 * 60 * 1000;
    private static final long REFRESH_TOKEN_EXPIRY = 7 * 24 * 60 * 60 * 1000;

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateAccessToken(
            String email,
            Long userId,
            AppRole role
    ) {

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role.name())
                .claim("type", "ACCESS")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(
            Long userId,
            AppRole role,
            Long sessionId
    ) {

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role.name())
                .claim("sessionId", sessionId)
                .claim("type", "REFRESH")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateAccessToken(String token) {
        Claims claims = parseToken(token);

        if (!"ACCESS".equals(claims.get("type", String.class))) {
            throw new JwtException("Invalid access token");
        }

        return claims;
    }

    public Claims validateRefreshToken(String token) {
        Claims claims = parseToken(token);

        if (!"REFRESH".equals(claims.get("type", String.class))) {
            throw new JwtException("Invalid refresh token");
        }

        return claims;
    }

    private Claims parseToken(String token) {

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            throw new JwtException("Token expired");

        } catch (JwtException e) {
            throw new JwtException("Invalid token");
        }
    }

    public Long extractUserId(Claims claims) {
        return claims.get("userId", Long.class);
    }

    public AppRole extractRole(Claims claims) {
        return AppRole.valueOf(claims.get("role", String.class));
    }

    public Long extractSessionId(Claims claims) {
        return claims.get("sessionId", Long.class);
    }
}
