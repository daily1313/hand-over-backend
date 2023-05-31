package com.example.handoverbackend.controller.report;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.report.BoardReportRequestDto;
import com.example.handoverbackend.dto.report.MatchReportRequestDto;
import com.example.handoverbackend.dto.report.MemberReportRequestDto;
import com.example.handoverbackend.repository.member.MemberRepository;
import com.example.handoverbackend.service.report.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static com.example.handoverbackend.factory.AuthenticationMaker.createAuthentication;
import static com.example.handoverbackend.factory.MemberMaker.createMember;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @InjectMocks
    ReportController reportController;
    @Mock
    ReportService reportService;
    @Mock
    MemberRepository memberRepository;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
    }

    @Test
    @DisplayName("유저 신고")
    void reportMember() throws Exception {
        //given
        MemberReportRequestDto requestDto = new MemberReportRequestDto(1l, "report");
        Member member = createMember();
        Authentication authentication = createAuthentication(member);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));


        // when, then
        mockMvc.perform(
                post("/api/reports/members")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk());

        verify(reportService).reportMember(requestDto, member);
    }

    @Test
    @DisplayName("게시글 신고")
    void reportBoard() throws Exception {
        // given
        BoardReportRequestDto requestDto = new BoardReportRequestDto(1L, "내용");
        Member member = createMember();
        Authentication authentication = createAuthentication(member);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));


        // when, then
        mockMvc.perform(
                post("/api/reports/boards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk());

        verify(reportService).reportBoard(requestDto, member);
    }

    @Test
    @DisplayName("매칭 신고")
    void reportMatch() throws Exception {
        //given
        MatchReportRequestDto requestDto = new MatchReportRequestDto(1L, "내용");
        Member member = createMember();
        Authentication authentication = createAuthentication(member);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when, when
        mockMvc.perform(
                post("/api/reports/matches")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk());

        verify(reportService).reportMatch(requestDto, member);
    }
}
