package com.tiffinservice.boxservice.auth.util;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHashGenerator {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostConstruct
    public void generatePasswordHash() {

        String rawPassword = "Admin@123";

        String hash = passwordEncoder.encode(rawPassword);

        System.out.println("========================================");
        System.out.println("RAW PASSWORD : " + rawPassword);
        System.out.println("BCRYPT HASH  : " + hash);
        System.out.println("========================================");
    }
}
