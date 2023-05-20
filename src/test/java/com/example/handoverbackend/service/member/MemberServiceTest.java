package com.example.handoverbackend.service.member;

import static com.example.handoverbackend.factory.MemberMaker.createMember;
import static com.example.handoverbackend.factory.MemberMaker.createMember2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.member.MemberEditRequestDto;
import com.example.handoverbackend.dto.member.MemberFindAllWithPagingResponseDto;
import com.example.handoverbackend.dto.member.MemberResponseDto;
import com.example.handoverbackend.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;



    @Test
    @DisplayName("회원 전체 조회 테스트")
    void findAllMembersTest() {
        //given
        List<Member> memberList = new ArrayList<>();
        Member member1 = createMember();
        Member member2 = createMember2();
        memberList.add(member1);
        memberList.add(member2);
        Page<Member> members = new PageImpl<>(memberList);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").descending());
        given(memberRepository.findAll(pageable)).willReturn(members);

        //when
        MemberFindAllWithPagingResponseDto result = memberService.findAllMembers(0);

        //then
        assertThat(result.getMembers().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("회원 정보 수정 테스트")
    void editMemberInfoTest() {
        //given
        Member member = createMember();
        MemberEditRequestDto req = new MemberEditRequestDto("username1","password1");

        //when
        memberService.editMemberInfo(member, req);

        //then
        assertThat(member.getUsername()).isEqualTo("username1");
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void deleteMemberTest() {
        //given
        Member member = createMember();


        //when
        memberService.deleteMember(member);

        //then
        verify(memberRepository).delete(member);
    }
}
