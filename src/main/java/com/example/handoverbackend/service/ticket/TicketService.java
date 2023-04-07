package com.example.handoverbackend.service.ticket;


import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.ticket.Ticket;
import com.example.handoverbackend.dto.page.PageInfoDto;
import com.example.handoverbackend.dto.ticket.TicketCreateRequestDto;
import com.example.handoverbackend.dto.ticket.TicketEditRequestDto;
import com.example.handoverbackend.dto.ticket.TicketFindAllWithPagingResponseDto;
import com.example.handoverbackend.dto.ticket.TicketResponseDto;
import com.example.handoverbackend.exception.TicketNotFoundException;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.repository.ticket.TicketRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TicketService {

    private static final String CHANGE_TICKET_SOLD_OUT_STATUS_SUCCESS_MESSAGE = "판매완료 처리되었습니다.";
    private static final String CHANGE_TICKET_ON_SALE_STATUS_SUCCESS_MESSAGE = "판매중 상태로 처리되었습니다.";
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String DEFAULT_PAGE_SORT = "id";
    private static final String PRICE_PAGE_SORT = "price";

    private final TicketRepository ticketRepository;

    @Transactional
    public TicketResponseDto writeTicketPost(Member seller, TicketCreateRequestDto req) {
        Ticket ticket = Ticket.builder()
                .seller(seller)
                .category(req.getCategory())
                .ticketName(req.getTicketName())
                .address(req.getAddress())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .detailsContent(req.getDetailsContent())
                .price(req.getPrice())
                .isOnSale(true)
                .build();
        ticketRepository.save(ticket);
        return TicketResponseDto.toDto(ticket);
    }

    @Transactional
    public String changeTicketSoldOutStatus(Member seller, Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(TicketNotFoundException::new);
        ticket.validateSeller(seller);
        ticket.validateIsSoldOut(ticket);
        ticket.changeSoldOutTicketStatus();
        ticketRepository.save(ticket);
        return CHANGE_TICKET_SOLD_OUT_STATUS_SUCCESS_MESSAGE;
    }

    @Transactional
    public String changeTicketOnSaleStatus(Member seller, Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(TicketNotFoundException::new);
        ticket.validateSeller(seller);
        ticket.validateIsOnSale(ticket);
        ticket.changeOnSaleTicketStatus();
        ticketRepository.save(ticket);
        return CHANGE_TICKET_ON_SALE_STATUS_SUCCESS_MESSAGE;
    }

    @Transactional
    public TicketResponseDto editTicketPost(Member seller, TicketEditRequestDto req, Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(TicketNotFoundException::new);
        ticket.validateSeller(seller);
        ticket.editTicketInfo(req);
        return TicketResponseDto.toDto(ticket);
    }

    @Transactional(readOnly = true)
    public TicketResponseDto getTicketPost(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(TicketNotFoundException::new);
        return TicketResponseDto.toDto(ticket);
    }

    @Transactional(readOnly = true)
    public TicketFindAllWithPagingResponseDto getAllTicketPostsWithPaging(int page) {
        PageRequest pageRequest = getPageRequest(page);
        Page<Ticket> tickets = ticketRepository.findAll(pageRequest);
        List<TicketResponseDto> allTickets = tickets.stream()
                .map(TicketResponseDto::toDto)
                .collect(Collectors.toList());
        return TicketFindAllWithPagingResponseDto.toDto(allTickets, new PageInfoDto(tickets));
    }

    @Transactional(readOnly = true)
    public TicketFindAllWithPagingResponseDto getAllTicketPostWithPagingBySearchingTicketName(String ticketName, int page) {
        PageRequest pageRequest = getPageRequest(page);
        Page<Ticket> tickets = ticketRepository.findAllByTicketNameContaining(ticketName, pageRequest);
        List<TicketResponseDto> allTickets = tickets.stream()
                .map(TicketResponseDto::toDto)
                .collect(Collectors.toList());
        return TicketFindAllWithPagingResponseDto.toDto(allTickets, new PageInfoDto(tickets));
    }

    @Transactional(readOnly = true)
    public TicketFindAllWithPagingResponseDto getAllTicketPostWithPagingBySearchingAddress(String address, int page) {
        PageRequest pageRequest = getPageRequest(page);
        Page<Ticket> tickets = ticketRepository.findAllByAddressContaining(address, pageRequest);
        List<TicketResponseDto> allTickets = tickets.stream()
                .map(TicketResponseDto::toDto)
                .collect(Collectors.toList());
        return TicketFindAllWithPagingResponseDto.toDto(allTickets, new PageInfoDto(tickets));
    }

    @Transactional(readOnly = true)
    public TicketFindAllWithPagingResponseDto getAllTicketPostWithPagingOrderByPriceAsc(int page) {
        PageRequest pageRequest = getPageRequestOrderByPrice(page);
        Page<Ticket> tickets = ticketRepository.findAllByOrderByPrice(pageRequest);
        List<TicketResponseDto> allTickets = tickets.stream()
                .map(TicketResponseDto::toDto)
                .collect(Collectors.toList());
        return TicketFindAllWithPagingResponseDto.toDto(allTickets, new PageInfoDto(tickets));
    }

    private PageRequest getPageRequest(Integer page) {
        return PageRequest.of(page, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_PAGE_SORT).descending());
    }

    private PageRequest getPageRequestOrderByPrice(Integer page) {
        return PageRequest.of(page, DEFAULT_PAGE_SIZE, Sort.by(PRICE_PAGE_SORT).ascending());
    }
}
