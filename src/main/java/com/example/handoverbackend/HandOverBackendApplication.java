package com.example.handoverbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@PropertySource("classpath:/.env")
public class HandOverBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandOverBackendApplication.class, args);
    }
}
