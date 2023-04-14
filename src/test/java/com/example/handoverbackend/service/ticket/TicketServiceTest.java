package com.example.handoverbackend.service.ticket;

import static com.example.handoverbackend.factory.MemberMaker.createMember;
import static com.example.handoverbackend.factory.TicketMaker.createTicket;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.handoverbackend.domain.favorite.TicketFavorite;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.ticket.Category;
import com.example.handoverbackend.domain.ticket.Ticket;
import com.example.handoverbackend.dto.ticket.TicketCreateRequestDto;
import com.example.handoverbackend.dto.ticket.TicketFindAllWithPagingResponseDto;
import com.example.handoverbackend.dto.ticket.TicketResponseDto;
import com.example.handoverbackend.repository.ticket.TicketFavoriteRepository;
import com.example.handoverbackend.repository.ticket.TicketRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @InjectMocks
    TicketService ticketService;

    @Mock
    TicketRepository ticketRepository;

    @Mock
    TicketFavoriteRepository ticketFavoriteRepository;

    @DisplayName("티켓 판매글 작성 테스트")
    @Test
    void writeTicketPostTest() {
        //given
        Ticket ticket = createTicket();
        TicketCreateRequestDto req = new TicketCreateRequestDto(Category.공연, "아이유 콘서트", "경기 용인시", LocalDate.parse("2023-04-12"), LocalDate.parse("2023-04-12"), "공연", 150000);
        given(ticketRepository.save(any())).willReturn(ticket);

        //when
        TicketResponseDto ticketResponseDto = ticketService.writeTicketPost(ticket.getSeller(), req);

        //then
        assertThat(ticketResponseDto.getAddress()).isEqualTo("경기 용인시");
    }

    @DisplayName("티켓 전체 조회 테스트(최신순)")
    @Test
    void getAllTicketPostsWithPagingTest() {
        //given
        PageRequest pageRequest = PageRequest.of(1, 10, Sort.by("id").descending());
        List<Ticket> tickets = List.of(createTicket(), createTicket());
        Page<Ticket> allTickets = new PageImpl<>(tickets);
        given(ticketRepository.findAll(pageRequest)).willReturn(allTickets);

        //when
        TicketFindAllWithPagingResponseDto result = ticketService.getAllTicketPostsWithPaging(1);

        //then
        assertThat(result.getTickets().size()).isEqualTo(2);
    }

    @DisplayName("티켓 전체 조회 테스트(가격 오름차순)")
    @Test
    void getAllTicketPostWithPagingOrderByPriceAscTest() {
        //given
        Ticket ticket = new Ticket(createMember(), Category.캠핑, "시흥 숙소", "경기 시흥시", LocalDate.parse("2023-04-15"),LocalDate.parse("2023-04-19"), "숙박", 50000, true);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("price").ascending());
        List<Ticket> tickets = List.of(ticket, createTicket());
        given(ticketRepository.findAllByOrderByPrice(pageRequest)).willReturn(new PageImpl<>(tickets, pageRequest, tickets.size()));

        //when
        TicketFindAllWithPagingResponseDto result = ticketService.getAllTicketPostWithPagingOrderByPriceAsc(0);

        //then
        assertThat(result.getTickets().get(0).getPrice()).isEqualTo(50000);
    }

    @DisplayName("티켓 전체 조회 테스트(가격 내림차순)")
    @Test
    void getAllTicketPostWithPagingOrderByPriceDescTest() {
        //given
        Ticket ticket1 = new Ticket(createMember(), Category.캠핑, "시흥 숙소", "경기 시흥시", LocalDate.parse("2023-04-15"), LocalDate.parse("2023-04-19"), "숙박", 50000, true);
        Ticket ticket2 = new Ticket(createMember(), Category.숙소, "제주도 여행", "제주도", LocalDate.parse("2023-05-01"), LocalDate.parse("2023-05-04"), "여행", 150000, true);
        List<Ticket> tickets = List.of(ticket1);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("price").descending());
        given(ticketRepository.findAllByOrderByPriceDesc(pageRequest)).willReturn(new PageImpl<>(tickets, pageRequest, tickets.size()));

        //when
        TicketFindAllWithPagingResponseDto result = ticketService.getAllTicketPostWithPagingOrderByPriceDesc(0);

        //then
        assertThat(result.getTickets().get(0).getPrice()).isEqualTo(50000);
    }

    @DisplayName("티켓 검색 조회 테스트(티켓이름으로 검색)")
    @Test
    void getAllTicketPostWithPagingBySearchingTicketNameTest() {
        String ticketName = "시흥";
        Ticket ticket1 = new Ticket(createMember(), Category.캠핑, "시흥 숙소", "경기 시흥시", LocalDate.parse("2023-04-15"), LocalDate.parse("2023-04-19"), "숙박", 50000, true);
        Ticket ticket2 = new Ticket(createMember(), Category.숙소, "제주도 여행", "제주도", LocalDate.parse("2023-05-01"), LocalDate.parse("2023-05-04"), "여행", 150000, true);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id").descending());
        List<Ticket> tickets = List.of(ticket1);
        given(ticketRepository.findAllByTicketNameContaining(ticketName, pageRequest)).willReturn(new PageImpl<>(tickets));

        //when
        TicketFindAllWithPagingResponseDto result = ticketService.getAllTicketPostWithPagingBySearchingTicketName(ticketName, 0);

        //then
        assertThat(result.getTickets().size()).isEqualTo(1);
        assertThat(result.getTickets().get(0).getTicketName()).isEqualTo(ticket1.getTicketName());
    }

    @DisplayName("티켓 검색 조회 테스트(주소로 검색)")
    @Test
    void getAllTicketPostWithPagingBySearchingAddress() {
        String address = "시흥";
        Ticket ticket1 = new Ticket(createMember(), Category.캠핑, "시흥 숙소", "경기 시흥시", LocalDate.parse("2023-04-15"), LocalDate.parse("2023-04-19"), "숙박", 50000, true);
        Ticket ticket2 = new Ticket(createMember(), Category.숙소, "제주도 여행", "제주도", LocalDate.parse("2023-05-01"), LocalDate.parse("2023-05-04"), "여행", 150000, true);
        List<Ticket> tickets = List.of(ticket1);

        given(ticketRepository.findAllByAddressContaining(address, PageRequest.of(0, 10, Sort.by("id").descending()))).willReturn(new PageImpl<>(tickets));

        //when
        TicketFindAllWithPagingResponseDto result = ticketService.getAllTicketPostWithPagingBySearchingAddress(address, 0);

        //then
        assertThat(result.getTickets().size()).isEqualTo(1);
        assertThat(result.getTickets().get(0).getAddress()).isEqualTo(ticket1.getAddress());
    }

    @DisplayName("티켓 판매글 즐겨찾기 테스트")
    @Test
    void createFavoriteTicketPostTest() {
        //given
        Member member = createMember();
        Ticket ticket = createTicket();

        given(ticketRepository.findById(1L)).willReturn(Optional.of(ticket));
        given(ticketFavoriteRepository.existsByTicketAndMember(ticket, member)).willReturn(false);

        //when
        String result = ticketService.updateFavoriteTicketPost(1L, member);

        //then
        assertThat(result).isEqualTo("즐겨찾기를 하였습니다.");
        verify(ticketFavoriteRepository).save(any());
    }

    @DisplayName("티켓 판매글 즐겨찾기 취소 테스트")
    @Test
    void deleteFavoriteTicketPostTest() {
        //given
        Member member = createMember();
        Ticket ticket = createTicket();
        TicketFavorite ticketFavorite = TicketFavorite.createFavorite(ticket, member);

        given(ticketRepository.findById(1L)).willReturn(Optional.of(ticket));
        given(ticketFavoriteRepository.existsByTicketAndMember(ticket, member)).willReturn(true);
        given(ticketFavoriteRepository.findByTicketAndMember(ticket, member)).willReturn(Optional.of(ticketFavorite));

        //when
        String result = ticketService.updateFavoriteTicketPost(1L, member);

        //then
        assertThat(result).isEqualTo("즐겨찾기가 해제되었습니다.");
        verify(ticketFavoriteRepository).delete(any());
    }

    @DisplayName("즐겨찾기한 티켓 판매글 전체목록 조회 테스트")
    @Test
    void findFavoriteTicketPostsTest() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id").descending());
        Member member = createMember();
        List<TicketFavorite> favorites = new ArrayList<>();
        favorites.add(TicketFavorite.createFavorite(createTicket(member), member));
        Page<TicketFavorite> favoritesWithPaging = new PageImpl<>(favorites);
        given(ticketFavoriteRepository.findAllByMember(member, pageRequest)).willReturn(favoritesWithPaging);

        //when
        TicketFindAllWithPagingResponseDto result = ticketService.findFavoriteTicketPosts(0, member);

        //then
        assertThat(result.getTickets().size()).isEqualTo(1);

    }

    @DisplayName("티켓 판매글 상태 판매완료로 변경 테스트")
    @Test
    void changeTicketSoldOutStatusTest() {
        //given
        Member member = createMember();
        Ticket ticket = createTicket(member);
        given(ticketRepository.findById(1L)).willReturn(Optional.of(ticket));

        //when
        String result = ticketService.changeTicketSoldOutStatus(member, 1L);

        //then
        assertThat(result).isEqualTo("판매완료 처리되었습니다.");
        assertThat(ticket.isSoldOut()).isTrue();
    }

    @DisplayName("티켓 판매글 상태 판매중으로 변경 테스트")
    @Test
    void changeTicketOnSaleStatusTest() {
        //given
        Member member = createMember();
        Ticket ticket = createTicket(member);
        ticket.changeSoldOutTicketStatus();
        given(ticketRepository.findById(1L)).willReturn(Optional.of(ticket));

        //when
        String result = ticketService.changeTicketOnSaleStatus(member, 1L);

        //then
        assertThat(result).isEqualTo("판매중 상태로 처리되었습니다.");
        assertThat(ticket.isOnSale()).isTrue();
    }
}
