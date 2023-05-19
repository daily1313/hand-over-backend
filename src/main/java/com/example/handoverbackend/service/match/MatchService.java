package com.example.handoverbackend.service.match;


import static com.example.handoverbackend.domain.favorite.MatchFavorite.createFavorite;

import com.example.handoverbackend.domain.favorite.MatchFavorite;
import com.example.handoverbackend.domain.match.Category;
import com.example.handoverbackend.domain.match.Match;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.match.MatchByMemberResponseDto;
import com.example.handoverbackend.dto.match.MatchFindAllByMemberWithPagingResponseDto;
import com.example.handoverbackend.dto.page.PageInfoDto;
import com.example.handoverbackend.dto.match.MatchCreateRequestDto;
import com.example.handoverbackend.dto.match.MatchEditRequestDto;
import com.example.handoverbackend.dto.match.MatchFindAllWithPagingResponseDto;
import com.example.handoverbackend.dto.match.MatchResponseDto;
import com.example.handoverbackend.exception.AlreadyMatchingException;
import com.example.handoverbackend.exception.AlreadyMatchedException;
import com.example.handoverbackend.exception.FavoriteNotFoundException;
import com.example.handoverbackend.exception.MemberNotEqualsException;
import com.example.handoverbackend.exception.MatchNotFoundException;
import com.example.handoverbackend.repository.match.MatchFavoriteRepository;
import com.example.handoverbackend.repository.match.MatchRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MatchService {

    private static final String CHANGE_MATCH_STATUS_SUCCESS_MESSAGE = "매칭글 상태를 변경하였습니다.";
    private static final String SUCCESS_CREATE_FAVORITE = "즐겨찾기를 하였습니다.";
    private static final String SUCCESS_DELETE_FAVORITE = "즐겨찾기가 해제되었습니다.";
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String DEFAULT_PAGE_SORT = "id";
    private static final String PRICE_PAGE_SORT = "price";

    private final MatchRepository matchRepository;
    private final MatchFavoriteRepository matchFavoriteRepository;

    @Transactional
    public MatchResponseDto writeMatchPost(Member seller, MatchCreateRequestDto req) {
        Match match = Match.builder()
                .seller(seller)
                .category(req.getCategory())
                .matchName(req.getMatchName())
                .address(req.getAddress())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .detailsContent(req.getDetailsContent())
                .price(req.getPrice())
                .precaution(req.getPrecaution())
                .isMatched(false)
                .build();
        matchRepository.save(match);
        return MatchResponseDto.toDto(match);
    }

    @Transactional
    public MatchFindAllByMemberWithPagingResponseDto findAllMatchesPostsByMember(Member member, int page) {
        PageRequest pageRequest = getPageRequest(page);
        Page<Match> matches = matchRepository.findAllBySeller(member, pageRequest);
        List<MatchByMemberResponseDto> allMatches = matches.stream()
                .map(MatchByMemberResponseDto::toDto)
                .collect(Collectors.toList());
        return MatchFindAllByMemberWithPagingResponseDto.toDto(allMatches, new PageInfoDto(matches));
    }

    @Transactional(readOnly = true)
    public MatchFindAllWithPagingResponseDto getAllMatchesPostsWithPaging(int page) {
        PageRequest pageRequest = getPageRequest(page);
        Page<Match> matches = matchRepository.findAll(pageRequest);
        List<MatchResponseDto> allMatches = matches.stream()
                .map(MatchResponseDto::toDto)
                .collect(Collectors.toList());
        return MatchFindAllWithPagingResponseDto.toDto(allMatches, new PageInfoDto(matches));
    }

    @Transactional(readOnly = true)
    public MatchFindAllWithPagingResponseDto getAllMatchesPostsByCategoryWithPaging(Category category, int page) {
        PageRequest pageRequest = getPageRequest(page);
        Page<Match> matches = matchRepository.findAllByCategory(category, pageRequest);
        List<MatchResponseDto> allMatches = matches.stream()
                .map(MatchResponseDto::toDto)
                .collect(Collectors.toList());
        return MatchFindAllWithPagingResponseDto.toDto(allMatches, new PageInfoDto(matches));
    }

    @Transactional(readOnly = true)
    public MatchFindAllWithPagingResponseDto getAllMatchesPostWithPagingBySearchingMatchNameOrAddress(String keyword, int page) {
        Pageable pageRequest = getPageRequest(page);
        Page<Match> matches = matchRepository.findAllByMatchNameContainingOrAddressContaining(keyword, keyword, pageRequest);
        List<MatchResponseDto> allMatches = matches.stream()
                .map(MatchResponseDto::toDto)
                .collect(Collectors.toList());
        return MatchFindAllWithPagingResponseDto.toDto(allMatches, new PageInfoDto(matches));
    }

    @Transactional(readOnly = true)
    public MatchFindAllWithPagingResponseDto getAllMatchesPostWithPagingOrderByPriceAsc(int page) {
        PageRequest pageRequest = getPageRequestOrderByPrice(page);
        Page<Match> matches = matchRepository.findAllByOrderByPrice(pageRequest);
        List<MatchResponseDto> allMatches = matches.stream()
                .map(MatchResponseDto::toDto)
                .collect(Collectors.toList());
        return MatchFindAllWithPagingResponseDto.toDto(allMatches, new PageInfoDto(matches));
    }

    @Transactional(readOnly = true)
    public MatchFindAllWithPagingResponseDto getAllMatchesPostWithPagingOrderByPriceDesc(int page) {
        PageRequest pageRequest = getPageRequestOrderByPriceDesc(page);
        Page<Match> matches = matchRepository.findAllByOrderByPriceDesc(pageRequest);
        List<MatchResponseDto> allMatches = matches.stream()
                .map(MatchResponseDto::toDto)
                .collect(Collectors.toList());
        return MatchFindAllWithPagingResponseDto.toDto(allMatches, new PageInfoDto(matches));
    }

    @Transactional(readOnly = true)
    public MatchResponseDto getMatchPost(Long id) {
        Match match = matchRepository.findById(id).orElseThrow(MatchNotFoundException::new);
        return MatchResponseDto.toDto(match);
    }

    @Transactional
    public String changeMatchStatus(Member seller, Long id) {
        Match match = matchRepository.findById(id).orElseThrow(MatchNotFoundException::new);
        validateSeller(match, seller);
        match.changeStatus();
        matchRepository.save(match);
        return CHANGE_MATCH_STATUS_SUCCESS_MESSAGE;
    }

    @Transactional
    public MatchResponseDto editMatchPost(Member seller, MatchEditRequestDto req, Long id) {
        Match match = matchRepository.findById(id).orElseThrow(MatchNotFoundException::new);
        validateSeller(match, seller);
        match.editMatchInfo(req);
        matchRepository.save(match);
        return MatchResponseDto.toDto(match);
    }

    @Transactional
    public String updateMatchFavoritePost(Long id, Member member) {
        Match match = matchRepository.findById(id).orElseThrow(MatchNotFoundException::new);
        if(hasFavoriteMatchPost(match, member)) {
            deleteFavoriteMatchPost(match, member);
            return SUCCESS_DELETE_FAVORITE;
        }
        createFavoriteMatchPost(match, member);
        return SUCCESS_CREATE_FAVORITE;
    }

    @Transactional(readOnly = true)
    public MatchFindAllWithPagingResponseDto findFavoriteMatchesPosts(Integer page, Member member) {
        PageRequest pageRequest = getPageRequest(page);
        Page<MatchFavorite> favorites = matchFavoriteRepository.findAllByMember(member, pageRequest);
        List<MatchResponseDto> allTickets = favorites.stream()
                .map(MatchFavorite::getMatch)
                .map(MatchResponseDto::toDto)
                .toList();
        return MatchFindAllWithPagingResponseDto.toDto(allTickets, new PageInfoDto(favorites));
    }

    private void deleteFavoriteMatchPost(Match match, Member member) {
        MatchFavorite matchFavorite = matchFavoriteRepository.findByMatchAndMember(match, member)
                .orElseThrow(FavoriteNotFoundException::new);
        matchFavoriteRepository.delete(matchFavorite);
    }

    private void createFavoriteMatchPost(Match match, Member member) {
        MatchFavorite matchFavorite = createFavorite(match, member);
        matchFavoriteRepository.save(matchFavorite);
    }

    private boolean hasFavoriteMatchPost(Match match, Member member) {
        return matchFavoriteRepository.existsByMatchAndMember(match, member);
    }

    private PageRequest getPageRequest(Integer page) {
        return PageRequest.of(page, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_PAGE_SORT).descending());
    }

    private PageRequest getPageRequestOrderByPrice(Integer page) {
        return PageRequest.of(page, DEFAULT_PAGE_SIZE, Sort.by(PRICE_PAGE_SORT).ascending());
    }

    private PageRequest getPageRequestOrderByPriceDesc(Integer page) {
        return PageRequest.of(page, DEFAULT_PAGE_SIZE, Sort.by(PRICE_PAGE_SORT).descending());
    }

    private void validateSeller(Match match, Member findMember) {
        if(!match.isSeller(findMember)) {
            throw new MemberNotEqualsException();
        }
    }
}
