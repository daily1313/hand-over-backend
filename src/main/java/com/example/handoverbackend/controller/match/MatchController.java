package com.example.handoverbackend.controller.match;

import com.example.handoverbackend.domain.match.Category;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.match.MatchCreateRequestDto;
import com.example.handoverbackend.dto.match.MatchEditRequestDto;
import com.example.handoverbackend.exception.MemberNotFoundException;
import com.example.handoverbackend.repository.member.MemberRepository;
import com.example.handoverbackend.response.Response;
import com.example.handoverbackend.service.match.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class MatchController {

    private static final String DEFAULT_PAGE = "0";

    private final MatchService matchService;
    private final MemberRepository memberRepository;

    @Operation(summary = "매칭글 작성", description = "매칭글을 작성합니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/matches")
    public Response writeMatchPost(@Valid @RequestBody MatchCreateRequestDto req) {
        Member seller = getPrincipal();
        return Response.success(matchService.writeMatchPost(seller, req));
    }

    @Operation(summary = "매칭글 전체 조회", description = "매칭글을 전체 조회합니다(최신순)")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/matches")
    public Response getAllMatchesPostsWithPaging(@RequestParam(defaultValue = DEFAULT_PAGE) Integer page) {
        return Response.success(matchService.getAllMatchesPostsWithPaging(page));
    }

    @Operation(summary = "사용자가 작성한 매칭글 전체 조회", description = "사용자가 작성한 매칭글 전체 조회(최신순)")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/matches/posts")
    public Response getAllMatchesPostByMemberWithPaging(@RequestParam(defaultValue = DEFAULT_PAGE) Integer page) {
        Member seller = getPrincipal();
        return Response.success(matchService.findAllMatchesPostsByMember(seller, page));
    }

    @Operation(summary = "카테고리별 매칭글 조회", description = "카테고리에 해당하는 매칭글을 조회합니다(최신순)")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/matches/category")
    public Response getAllMatchesPostsByCategoryWithPaging(@RequestParam(value = "category", defaultValue = "전체") Category category, @RequestParam(defaultValue = DEFAULT_PAGE) Integer page) {
        return Response.success(matchService.getAllMatchesPostsByCategoryWithPaging(category, page));
    }

    @Operation(summary = "매칭글 검색 조회(매칭글 제목 및 주소로 검색)", description = "매칭글을 전체 조회합니다(매칭글 제목 및 주소로 검색)")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/matches/search")
    public Response getAllMatchesPostsWithPagingBySearchingMatchName(@RequestParam String keyword,
                                                                  @RequestParam(defaultValue = DEFAULT_PAGE) Integer page) {
        return Response.success(matchService.getAllMatchesPostWithPagingBySearchingMatchNameOrAddress(keyword, page));
    }

    @Operation(summary = "매칭글 전체 조회(가격 낮은 순)", description = "매칭글을 전체 조회합니다(가격 낮은 순 조회)")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/matches/low-price")
    public Response getAllMatchPostsWithPagingOrderByPriceAsc(@RequestParam(name = "page", defaultValue = DEFAULT_PAGE) Integer page) {
        return Response.success(matchService.getAllMatchesPostWithPagingOrderByPriceAsc(page));
    }

    @Operation(summary = "매칭글 전체 조회(가격 높은 순)", description = "매칭글을 전체 조회합니다(가격 높은 순 조회)")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/matches/high-price")
    public Response getAllMatchPostsWithPagingOrderByPriceDesc(@RequestParam(name = "page", defaultValue = DEFAULT_PAGE) Integer page) {
        return Response.success(matchService.getAllMatchesPostWithPagingOrderByPriceDesc(page));
    }
    @Operation(summary = "매칭글 단건 조회", description = "매칭글을 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/matches/{id}")
    public Response getMatchPost(@PathVariable Long id) {
        return Response.success(matchService.getMatchPost(id));
    }

    @Operation(summary = "매칭 상태 변경(매칭 완료-> 매칭중, 매칭중 -> 매칭완료로 변경)", description = "매칭글 상태를 매칭중, 매칭완료로 변경합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/matches/{id}/edit/matchStatus")
    public Response changeMatchStatus(@PathVariable Long id) {
        Member seller = getPrincipal();
        return Response.success(matchService.changeMatchStatus(seller, id));
    }

    @Operation(summary = "매칭글 정보 수정", description = "매칭글 정보를 수정합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/matches/{id}")
    public Response editMatchPost(@Valid @RequestBody MatchEditRequestDto req, @PathVariable Long id) {
        Member seller = getPrincipal();
        return Response.success(matchService.editMatchPost(seller, req, id));
    }

    @Operation(summary = "매칭글 즐겨찾기", description = "매칭글을 즐겨찾기 합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/matches/{id}/favorites")
    public Response favoriteMatchPost(@PathVariable Long id) {
        Member member = getPrincipal();
        return Response.success(matchService.updateMatchFavoritePost(id, member));
    }

    @Operation(summary = "매칭글 즐겨찾기 목록 조회", description = "즐겨찾기한 매칭글을 전체 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/matches/favorites")
    public Response getFavoriteAllMatchPost(@RequestParam(defaultValue = DEFAULT_PAGE) Integer page) {
        Member member = getPrincipal();
        return Response.success(matchService.findFavoriteMatchesPosts(page, member));
    }

    private Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName())
                .orElseThrow(MemberNotFoundException::new);
        return member;
    }
}
