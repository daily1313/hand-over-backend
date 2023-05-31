package com.example.handoverbackend.controller.comment;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.comment.CommentEditRequestDto;
import com.example.handoverbackend.dto.comment.MatchCommentCreateRequestDto;
import com.example.handoverbackend.repository.member.MemberRepository;
import com.example.handoverbackend.service.comment.MatchCommentService;
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
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MatchCommentControllerTest {

    @InjectMocks
    MatchCommentController matchCommentController;
    @Mock
    MatchCommentService matchCommentService;
    @Mock
    MemberRepository memberRepository;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(matchCommentController).build();
    }

    @DisplayName("매칭의 댓글 조회 테스트")
    @Test
    void findComments() throws Exception {
        //given
        Long request = 0L;
        Integer page = 0;

        //when
        mockMvc.perform(
            get("/api/match/comments")
                .param("matchId", String.valueOf(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //then
        verify(matchCommentService).findMatchComments(refEq(request), refEq(page));
    }

    @DisplayName("댓글을 작성합니다.")
    @Test
    void createComment() throws Exception {
        //given
        MatchCommentCreateRequestDto requestDto = new MatchCommentCreateRequestDto(1L, "content");
        Member member = createMember();
        Authentication authentication = createAuthentication(member);
        SecurityContextHolder.getContext()
            .setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName()))
            .willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                post("/api/match/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isCreated());

        //then
        verify(matchCommentService).createMatchComment(refEq(requestDto), refEq(member));
    }

    @DisplayName("댓글을 수정합니다.")
    @Test
    void editComment() throws Exception {
        //given
        Long id = 1L;
        CommentEditRequestDto requestDto = new CommentEditRequestDto("content");
        Member member = createMember();
        Authentication authentication = createAuthentication(member);
        SecurityContextHolder.getContext()
            .setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName()))
            .willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                patch("/api/match/comments/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk());

        //then
        verify(matchCommentService).editMatchComment(refEq(requestDto), refEq(member), refEq(id));
    }

    @DisplayName("댓글을 삭제합니다.")
    @Test
    void deleteComment() throws Exception {
        //given
        Long id = 1L;
        Member member = createMember();
        Authentication authentication = createAuthentication(member);
        SecurityContextHolder.getContext()
            .setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName()))
            .willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                delete("/api/match/comments/{id}", id))
            .andExpect(status().isOk());

        //then
        verify(matchCommentService).deleteMatchComment(refEq(member), refEq(id));
    }
}
