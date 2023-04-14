package com.example.handoverbackend.controller.ticket;

import static com.example.handoverbackend.factory.AuthenticationMaker.createAuthentication;
import static com.example.handoverbackend.factory.MemberMaker.createMember;
import static com.example.handoverbackend.factory.TicketMaker.createTicket;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.handoverbackend.domain.ticket.Ticket;
import com.example.handoverbackend.dto.ticket.TicketEditRequestDto;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.ticket.Category;
import com.example.handoverbackend.dto.ticket.TicketCreateRequestDto;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.service.ticket.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class TicketControllerTest {

    @InjectMocks
    TicketController ticketController;

    @Mock
    TicketService ticketService;

    @Mock
    MemberRepository memberRepository;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
    }

    @DisplayName("티켓 판매글 작성 테스트")
    @Test
    void writeTicketPostTest() throws Exception {
        //given
        TicketCreateRequestDto req = new TicketCreateRequestDto(Category.공연, "아이유 콘서트", "경기 용인시", LocalDate.parse("2023-04-12"), LocalDate.parse("2023-04-12"), "공연", 150000);
        Member member = createMember();
        Authentication authentication = createAuthentication(member);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
        ).andExpect(status().isCreated());

        //then
        verify(ticketService).writeTicketPost(refEq(member), refEq(req));
    }

    @DisplayName("티켓 판매글 전체 조회 테스트")
    @Test
    void getAllTicketPostsWithPagingTest() throws Exception {
        //given
        Integer page = 0;

        //when
        mockMvc.perform(
                get("/api/tickets")
        ).andExpect(status().isOk());

        //then
        verify(ticketService).getAllTicketPostsWithPaging(0);
    }

    @DisplayName("티켓 판매글 단건 조회 테스트")
    @Test
    void getTicketPostTest() throws Exception {
        //given
        Long id = 1L;

        //when
        mockMvc.perform(
                get("/api/tickets/{id}", id)
        ).andExpect(status().isOk());

        //then
        verify(ticketService).getTicketPost(1L);
    }

    @DisplayName("티켓 판매 상태 판매완료로 변경 테스트")
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
                patch("/api/tickets/{id}/edit/soldOut", id)
        ).andExpect(status().isOk());

        //then
        verify(ticketService).changeTicketSoldOutStatus(member, id);
    }

    @DisplayName("티켓 판매 상태 판매완료로 변경 테스트")
    @Test
    void changeTicketOnSaleStatusTest() throws Exception {
        //given
        Long id = 1L;
        Member member = createMember();
        Authentication authentication = createAuthentication(member);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                patch("/api/tickets/{id}/edit/onSale", id)
        ).andExpect(status().isOk());

        //then
        verify(ticketService).changeTicketOnSaleStatus(member, id);
    }

    @DisplayName("티켓 판매글 수정 테스트")
    @Test
    void editTicketPostTest() throws Exception {
        //given
        Long id = 1L;
        Member member = createMember();
        Ticket ticket = createTicket(member);
        TicketEditRequestDto req = new TicketEditRequestDto(Category.공연, "엑소 콘서트", "경기 용인시", LocalDate.parse("2023-04-12"), LocalDate.parse("2023-04-12"), "공연", 150000);
        ticket.editTicketInfo(req);
        Authentication authentication = createAuthentication(member);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));


        //when
        mockMvc.perform(
                patch("/api/tickets/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        //then
        verify(ticketService).editTicketPost(refEq(member), refEq(req), any());
        assertThat(ticket.getTicketName()).isEqualTo("엑소 콘서트");
    }

    @DisplayName("티켓 판매글 즐겨찾기 테스트")
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
                post("/api/tickets/{id}/favorites", id)
        ).andExpect(status().isOk());

        //then
        verify(ticketService).updateFavoriteTicketPost(id, member);
    }

    @DisplayName("티켓 판매글 즐겨찾기 목록 테스트")
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
                get("/api/tickets/favorites")
        ).andExpect(status().isOk());

        //then
        verify(ticketService).findFavoriteTicketPosts(page, member);
    }

    @DisplayName("티켓 판매글 검색 테스트(주소)")
    @Test
    void getAllTicketPostWithPagingBySearchingAddressTest() throws Exception {
        //given
        String address = "용인";
        Integer page = 0;

        //when
        mockMvc.perform(
                get("/api/tickets/search/address/{address}", address)
        ).andExpect(status().isOk());

        //then
        verify(ticketService).getAllTicketPostWithPagingBySearchingAddress(address, page);
    }

    @DisplayName("티켓 판매글 검색 테스트(이름)")
    @Test
    void getAllTicketPostWithPagingBySearchingTicketNameTest() throws Exception {
        //given
        String ticketName = "아이유";
        Integer page = 0;

        //when
        mockMvc.perform(
                get("/api/tickets/search/ticketName/{ticketName}", ticketName)
        ).andExpect(status().isOk());

        //then
        verify(ticketService).getAllTicketPostWithPagingBySearchingTicketName(ticketName, page);
    }
}
