package com.example.handoverbackend.controller.ticket;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.ticket.TicketCreateRequestDto;
import com.example.handoverbackend.dto.ticket.TicketEditRequestDto;
import com.example.handoverbackend.exception.MemberNotFoundException;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.response.Response;
import com.example.handoverbackend.service.ticket.TicketService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.Builder.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TicketController {

    private static final String DEFAULT_PAGE = "0";

    private final TicketService ticketService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "티켓 판매글 작성", notes = "티켓 판매글을 작성합니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/tickets")
    public Response writeTicketPost(@Valid @RequestBody TicketCreateRequestDto req) {
        Member seller = getPrincipal();
        return Response.success(ticketService.writeTicketPost(seller, req));
    }

    @ApiOperation(value = "티켓 판매글 단건 조회", notes = "티켓 판매글을 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/tickets/{id}")
    public Response getTicketPost(@PathVariable Long id) {
        return Response.success(ticketService.getTicketPost(id));
    }

    @ApiOperation(value = "티켓 판매 상태 변경(판매 완료료 변경)", notes = "티켓 판매 상태를 판매 완료로 변경합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/tickets/{id}/edit/soldOut")
    public Response changeTicketSoldOutStatus(@PathVariable Long id) {
        Member seller = getPrincipal();
        return Response.success(ticketService.changeTicketSoldOutStatus(seller, id));
    }

    @ApiOperation(value = "티켓 판매 상태 변경(판매중으로 변경)", notes = "티켓 판매 상태를 판매중으로 변경합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/tickets/{id}/edit/onSale")
    public Response changeTicketOnSaleStatus(@PathVariable Long id) {
        Member seller = getPrincipal();
        return Response.success(ticketService.changeTicketOnSaleStatus(seller, id));
    }

    @ApiOperation(value = "티켓 판매글 전체 조회", notes = "티켓 판매글을 전체 조회합니다(최신순)")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/tickets")
    public Response getAllTicketPostsWithPaging(@RequestParam(defaultValue = DEFAULT_PAGE) Integer page) {
        return Response.success(ticketService.getAllTicketPostsWithPaging(page));
    }

    @ApiOperation(value = "티켓 판매글 검색 조회(티켓이름으로 검색)", notes = "티켓 판매글을 전체 조회합니다(티켓이름으로 검색)")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/tickets/search/{ticketName}")
    public Response getAllTicketPostWithPagingBySearchingTicketName(@PathVariable String ticketName,
                                                                    @RequestParam(defaultValue = DEFAULT_PAGE) Integer page) {
        return Response.success(ticketService.getAllTicketPostWithPagingBySearchingTicketName(ticketName, page));
    }

    @ApiOperation(value = "티켓 판매글 전체 조회(가격 낮은 순)", notes = "티켓 판매글을 전체 조회합니다(가격 낮은 순 조회)")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/tickets/low-price")
    public Response getAllTicketPostWithPagingOrderByPriceAsc(@RequestParam(name = "page", defaultValue = "price:asc") Integer page) {
        return Response.success(ticketService.getAllTicketPostWithPagingOrderByPriceAsc(page));
    }

    @ApiOperation(value = "티켓 판매글 전체 조회(가격 높은 순)", notes = "티켓 판매글을 전체 조회합니다(가격 높은 순 조회)")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/tickets/high-price")
    public Response getAllTicketPostWithPagingOrderByPriceDesc(@RequestParam(name = "page", defaultValue = "price:desc") Integer page) {
        return Response.success(ticketService.getAllTicketPostWithPagingOrderByPriceDesc(page));
    }

    @ApiOperation(value = "티켓 판매글 검색 조회(주소로 검색)", notes = "티켓 판매글을 전체 조회합니다(주소로 검색)")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/tickets/search/{address}")
    public Response getAllTicketPostWithPagingBySearchingAddress(@PathVariable String address,
                                                                    @RequestParam(defaultValue = DEFAULT_PAGE) Integer page) {
        return Response.success(ticketService.getAllTicketPostWithPagingBySearchingAddress(address, page));
    }

    @ApiOperation(value = "티켓 정보 수정", notes = "티켓 정보를 수정합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/ticket/{id}")
    public Response editTicketPost(@Valid @RequestBody TicketEditRequestDto req, @PathVariable Long id) {
        Member seller = getPrincipal();
        return Response.success(ticketService.editTicketPost(seller, req, id));
    }

    @ApiOperation(value = "티켓 판매글 즐겨찾기", notes = "티켓 판매글을 즐겨찾기 합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/tickets/{id}/favorites")
    public Response favoriteTicketPost(@PathVariable Long id) {
        Member member = getPrincipal();
        return Response.success(ticketService.updateFavoriteTicketPost(id, member));
    }

    @ApiOperation(value = "티켓 판매글 즐겨찾기 목록 조회", notes = "즐겨찾기한 티켓 판매글을 전체 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/tickets/favorites")
    public Response getFavoriteAllTicketPost(@RequestParam(defaultValue = DEFAULT_PAGE) Integer page) {
        Member member = getPrincipal();
        return Response.success(ticketService.findFavoriteTicketPosts(page, member));
    }

    private Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName())
                .orElseThrow(MemberNotFoundException::new);
        return member;
    }
}
