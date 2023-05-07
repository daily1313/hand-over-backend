package com.example.handoverbackend.controller.member;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.member.MemberEditRequestDto;
import com.example.handoverbackend.dto.member.MemberSearchCondition;
import com.example.handoverbackend.exception.MemberNotFoundException;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.response.Response;
import com.example.handoverbackend.service.member.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "전체 회원 조회", notes = "전체 회원을 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/members")
    public Response findAllMembers(@PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return Response.success(memberService.findAllMembers(pageable));
    }

    @ApiOperation(value = "회원 단건 조회", notes = "회원 한 명을 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/members/{id}")
    public Response findMember(@PathVariable Long id) {
        return Response.success(memberService.findMember(id));
    }

    @ApiOperation(value = "전체 회원 조회(검색)", notes = "검색한 전체 회원을 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/members/search")
    public Response searchMembers(@RequestBody MemberSearchCondition condition,
                                  @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return Response.success(memberService.searchMembers(condition, pageable));
    }

    @ApiOperation(value = "회원 정보 수정", notes = "회원 정보를 수정합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/members")
    public Response editMemberInfo(@RequestBody MemberEditRequestDto memberEditRequestDto) {
        Member memberInfo = getPrincipal();
        return Response.success(memberService.editMemberInfo(memberInfo, memberEditRequestDto));
    }

    @ApiOperation(value = "회원 탈퇴", notes = "회원을 탈퇴합니다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/members")
    public Response deleteMember() {
        Member memberInfo = getPrincipal();
        return Response.success(memberService.deleteMember(memberInfo));
    }


    // 유저 정보를 가져오는 getPrincipal 함수
    private Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName())
            .orElseThrow(MemberNotFoundException::new);
        return member;
    }
}
