package com.example.handoverbackend.controller.match;

import com.example.handoverbackend.domain.match.Category;
import com.example.handoverbackend.domain.match.Match;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.match.MatchCreateRequestDto;
import com.example.handoverbackend.dto.match.MatchEditRequestDto;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.service.match.MatchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;

import static com.example.handoverbackend.factory.AuthenticationMaker.createAuthentication;
import static com.example.handoverbackend.factory.MatchMaker.createMatch;
import static com.example.handoverbackend.factory.MemberMaker.createMember;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MatchControllerTest {

    @InjectMocks
    MatchController matchController;

    @Mock
    MatchService matchService;

    @Mock
    MemberRepository memberRepository;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(matchController).build();
    }

    @DisplayName("매칭글 작성 테스트")
    @Test
    void writeTicketPostTest() throws Exception {
        //given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        MatchCreateRequestDto req = new MatchCreateRequestDto(Category.노인돌봄 , "노인 돌보기", "경기 용인시 처인구",
                LocalDateTime.parse("2023-04-12 12:00", formatter), LocalDateTime.parse("2023-04-12 12:00", formatter), "돌보기", 150000, "거동이 불편하니 조심하세요");
        Member member = createMember();
        Authentication authentication = createAuthentication(member);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
        ).andExpect(status().isCreated());

        //then
        verify(matchService).writeMatchPost(refEq(member), refEq(req));
    }

    @DisplayName("매칭글 전체 조회 테스트")
    @Test
    void getAllMatchesPostsWithPagingTest() throws Exception {
        //given
        Integer page = 0;

        //when
        mockMvc.perform(
                get("/api/matches")
        ).andExpect(status().isOk());

        //then
        verify(matchService).getAllMatchesPostsWithPaging(page);
    }

    @DisplayName("매칭글 단건 조회 테스트")
    @Test
    void getMatchPostTest() throws Exception {
        //given
        Long id = 1L;

        //when
        mockMvc.perform(
                get("/api/matches/{id}", id)
        ).andExpect(status().isOk());

        //then
        verify(matchService).getMatchPost(1L);
    }

    @DisplayName("매칭글 상태 변경 테스트")
    @Test
    void changeTicketSoldOutStatusTest() throws Exception {
        //given
        Long id = 1L;
        Member member = createMember();
        Authentication authentication = createAuthentication(member);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                patch("/api/matches/{id}/edit/matchStatus", id)
        ).andExpect(status().isOk());

        //then
        verify(matchService).changeMatchStatus(member, id);
    }

    @DisplayName("매칭글 수정 테스트")
    @Test
    void editMatchPostTest() throws Exception {
        //given
        Long id = 1L;
        Member member = createMember();
        Match match = createMatch(member);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        MatchEditRequestDto req = new MatchEditRequestDto(Category.노인돌봄 , "노인 배웅", "경기 용인시 처인구",
                LocalDateTime.parse("2023-04-12 12:00", formatter), LocalDateTime.parse("2023-04-12 12:00", formatter), "돌보기", 150000, "거동이 불편하니 조심하세요");
        match.editMatchInfo(req);
        Authentication authentication = createAuthentication(member);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                patch("/api/matches/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        //then
        verify(matchService).editMatchPost(refEq(member), refEq(req), any());
        assertThat(match.getMatchName()).isEqualTo("노인 배웅");
    }

    @DisplayName("매칭글 즐겨찾기 테스트")
    @Test
    void updateFavoriteTicketPost() throws Exception {
        //given
        Long id = 1L;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                post("/api/matches/{id}/favorites", id)
        ).andExpect(status().isOk());

        //then
        verify(matchService).updateMatchFavoritePost(id, member);
    }

    @DisplayName("매칭글 즐겨찾기 목록 테스트")
    @Test
    void findFavoriteTicketPosts() throws Exception {
        //given
        Integer page = 0;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                get("/api/matches/favorites")
        ).andExpect(status().isOk());

        //then
        verify(matchService).findFavoriteMatchesPosts(page, member);
    }

    @DisplayName("매칭글 검색 테스트(주소 및 티켓이름으로 검색)")
    @Test
    void getAllMatchesPostWithPagingBySearchingAddressTest() throws Exception {
        //given
        String keyword = "용인";
        Integer page = 0;

        //when
        mockMvc.perform(
                get("/api/matches/search")
                        .param("keyword", keyword)
        ).andExpect(status().isOk());

        //then
        verify(matchService).getAllMatchesPostWithPagingBySearchingMatchNameOrAddress(keyword, page);
    }
}
