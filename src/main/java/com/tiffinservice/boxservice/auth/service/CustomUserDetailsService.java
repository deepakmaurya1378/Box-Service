package com.tiffinservice.boxservice.auth.service;

import com.tiffinservice.boxservice.auth.security.CustomUserDetails;
import com.tiffinservice.boxservice.auth.enums.AppRole;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) {
        throw new UsernameNotFoundException("UserDetailsService is not used for login. Use AuthService.");
    }
}
