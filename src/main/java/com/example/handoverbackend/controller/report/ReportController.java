package com.example.handoverbackend.controller.report;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.report.BoardReportRequestDto;
import com.example.handoverbackend.dto.report.MemberReportRequestDto;
import com.example.handoverbackend.exception.MemberNotFoundException;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.response.Response;
import com.example.handoverbackend.service.report.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(value = "Report Controller", tags = "Report")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "유저 신고", notes = "유저를 신고합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reports/members")
    public Response reportMember(@Valid @RequestBody MemberReportRequestDto requestDto) {
        return Response.success(reportService.reportMember(requestDto, getPrincipal()));
    }

    @ApiOperation(value = "게시글 신고", notes = "게시글을 신고합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reports/boards")
    public Response reportBoard(@Valid @RequestBody BoardReportRequestDto requestDto) {
        return Response.success(reportService.reportBoard(requestDto, getPrincipal()));
    }

    private Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName())
            .orElseThrow(MemberNotFoundException::new);
        return member;
    }
}
