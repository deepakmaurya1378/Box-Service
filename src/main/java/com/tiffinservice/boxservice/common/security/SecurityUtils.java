package com.tiffinservice.boxservice.common.security;

import com.tiffinservice.boxservice.auth.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails)) {
            throw new IllegalStateException("Unauthenticated request");
        }

        return ((CustomUserDetails) auth.getPrincipal()).getId();
    }
}
