package com.example.handoverbackend.repository;

import com.example.handoverbackend.dto.member.MemberResponseDto;
import com.example.handoverbackend.dto.member.MemberSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    Page<MemberResponseDto> searchMember(MemberSearchCondition req, Pageable pageable);
}
