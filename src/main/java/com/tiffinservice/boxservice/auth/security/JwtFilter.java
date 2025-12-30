package com.tiffinservice.boxservice.auth.security;

import com.tiffinservice.boxservice.auth.enums.AppRole;
import com.tiffinservice.boxservice.auth.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();
        String method = request.getMethod();

        return path.startsWith("/auth")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")

                || (path.equals("/api/users/register") && "POST".equals(method))

                || (path.equals("/api/vendors/register") && "POST".equals(method))

                || path.startsWith("/api/vendors/nearby");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);

            Claims claims = jwtUtil.validateAccessToken(token);

            Long userId = jwtUtil.extractUserId(claims);
            AppRole role = jwtUtil.extractRole(claims);
            String username = claims.getSubject();

            CustomUserDetails userDetails =
                    new CustomUserDetails(
                            userId,
                            username,
                            null,
                            role,
                            true
                    );


            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException ex) {

            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                  "success": false,
                  "status": 401,
                  "message": "Invalid or expired access token",
                  "data": null
                }
            """);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
