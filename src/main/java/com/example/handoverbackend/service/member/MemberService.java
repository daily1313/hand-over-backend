package com.example.handoverbackend.service.member;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.member.MemberEditRequestDto;
import com.example.handoverbackend.dto.member.MemberFindRequestDto;
import com.example.handoverbackend.dto.member.MemberResponseDto;
import com.example.handoverbackend.dto.member.MemberSearchCondition;
import com.example.handoverbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final String SUCCESS_DELETE_MESSAGE = "회원탈퇴에 성공하였습니다.";

    // 회원 전체 조회
    @Transactional(readOnly = true)
    public Page<MemberResponseDto> findAllMembers(Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);
        Page<MemberResponseDto>  allMembers = members.map(MemberResponseDto::toDto);

        return allMembers;
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
