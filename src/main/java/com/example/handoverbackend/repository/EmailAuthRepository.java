package com.example.handoverbackend.repository;

import com.example.handoverbackend.domain.member.EmailAuth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {

    Optional<EmailAuth> findEmailAuthByEmail(String email);

    void deleteEmailAuthByEmail(String to);

    boolean existsByKey(String code);
}
