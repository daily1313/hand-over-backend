package com.example.handoverbackend.service.member;

import com.example.handoverbackend.domain.member.Authority;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.member.MemberEditRequestDto;
import com.example.handoverbackend.dto.member.MemberResponseDto;
import com.example.handoverbackend.dto.member.MemberSearchCondition;
import com.example.handoverbackend.exception.MemberNotFoundException;
import com.example.handoverbackend.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private static final String SUCCESS_DELETE_MESSAGE = "회원탈퇴에 성공하였습니다.";

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final PasswordEncoder encoder;

    @Transactional
    public void join(Member member) {
        String rawPassword = member.getPassword();
        String encPassword = encoder.encode(rawPassword);
        member.setPassword(encPassword);
        member.setRole(Authority.ROLE_USER);
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member findMember(String username) {
        Member member = memberRepository.findByUsername(username).orElseGet(() -> {
            return new Member();
        });
        return member;
    }

    // 회원 전체 조회
    @Transactional(readOnly = true)
    public Page<MemberResponseDto> findAllMembers(Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);
        Page<MemberResponseDto> allMembers = members.map(MemberResponseDto::toDto);

        return allMembers;
    }

    // 회원 단건 조회
    @Transactional(readOnly = true)
    public MemberResponseDto findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return MemberResponseDto.toDto(member);
    }

    // 회원 검색 조회(이름, 닉네임)
    @Transactional(readOnly = true)
    public Page<MemberResponseDto> searchMembers(MemberSearchCondition condition, Pageable pageable) {
        Page<MemberResponseDto> result = memberRepository.searchMember(condition, pageable);
        return result;
    }

    // 회원 정보 수정
    @Transactional
    public Member editMemberInfo(Member member, MemberEditRequestDto req) {
        member.editMember(req);
        memberRepository.save(member);
        return member;
    }

    // 회원 탈퇴
    @Transactional
    public String deleteMember(Member member) {
        memberRepository.delete(member);
        return SUCCESS_DELETE_MESSAGE;
    }
}
