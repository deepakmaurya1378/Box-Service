package com.tiffinservice.boxservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BoxServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoxServiceApplication.class, args);
    }
}
