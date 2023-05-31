package com.example.handoverbackend.repository.member;

import com.example.handoverbackend.domain.member.Member;

import com.example.handoverbackend.dto.member.MemberResponseDto;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Page<Member> findAllByNicknameContaining(String nickname, Pageable pageable);
}
