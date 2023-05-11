package com.example.handoverbackend.controller.report;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.report.BoardReportRequestDto;
import com.example.handoverbackend.dto.report.MatchReportRequestDto;
import com.example.handoverbackend.dto.report.MemberReportRequestDto;
import com.example.handoverbackend.exception.MemberNotFoundException;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.response.Response;
import com.example.handoverbackend.service.report.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "유저 신고", description = "유저를 신고합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reports/members")
    public Response reportMember(@Valid @RequestBody MemberReportRequestDto requestDto) {
        return Response.success(reportService.reportMember(requestDto, getPrincipal()));
    }

    @Operation(summary = "게시글 신고", description = "게시글을 신고합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reports/boards")
    public Response reportBoard(@Valid @RequestBody BoardReportRequestDto requestDto) {
        return Response.success(reportService.reportBoard(requestDto, getPrincipal()));
    }

    @Operation(summary = "매칭 신고", description = "매칭을 신고합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reports/matches")
    public Response reportMatch(@Valid @RequestBody MatchReportRequestDto requestDto) {
        return Response.success(reportService.reportMatch(requestDto, getPrincipal()));
    }

    private Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return memberRepository.findByUsername(authentication.getName())
            .orElseThrow(MemberNotFoundException::new);
    }
}
