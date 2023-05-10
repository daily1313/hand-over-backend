package com.example.handoverbackend.service.match;

import com.example.handoverbackend.domain.favorite.MatchFavorite;
import com.example.handoverbackend.domain.match.Category;
import com.example.handoverbackend.domain.match.Match;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.match.MatchCreateRequestDto;
import com.example.handoverbackend.dto.match.MatchFindAllWithPagingResponseDto;
import com.example.handoverbackend.dto.match.MatchResponseDto;
import com.example.handoverbackend.repository.match.MatchFavoriteRepository;
import com.example.handoverbackend.repository.match.MatchRepository;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.handoverbackend.factory.MatchMaker.createMatch;
import static com.example.handoverbackend.factory.MemberMaker.createMember;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @InjectMocks
    MatchService matchService;

    @Mock
    MatchRepository matchRepository;

    @Mock
    MatchFavoriteRepository matchFavoriteRepository;

    @DisplayName("매칭글 작성 테스트")
    @Test
    void writeMatchPostTest() {
        //given
        Match match = createMatch();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        MatchCreateRequestDto req = new MatchCreateRequestDto(Category.노인돌봄, "노인 돌보기", "경기 용인시 처인구",
            LocalDateTime.parse("2023-04-12 12:00", formatter), LocalDateTime.parse("2023-04-12 12:00", formatter), "돌보기", 150000, "거동이 불편하니 조심하세요");
        given(matchRepository.save(any())).willReturn(match);

        //when
        MatchResponseDto matchResponseDto = matchService.writeMatchPost(match.getSeller(), req);

        //then
        assertThat(matchResponseDto.getAddress()).isEqualTo("경기 용인시 처인구");
    }

    @DisplayName("매칭글 전체 조회 테스트(최신순)")
    @Test
    void getAllMatchesPostsWithPagingTest() {
        //given
        PageRequest pageRequest = PageRequest.of(1, 10, Sort.by("id").descending());
        List<Match> matches = List.of(createMatch(), createMatch());
        Page<Match> allMatches = new PageImpl<>(matches);
        given(matchRepository.findAll(pageRequest)).willReturn(allMatches);

        //when
        MatchFindAllWithPagingResponseDto result = matchService.getAllMatchesPostsWithPaging(1);

        //then
        assertThat(result.getMatches().size()).isEqualTo(2);
    }

    @DisplayName("매칭글 조회 테스트(가격 오름차순)")
    @Test
    void getAllMatchesPostWithPagingOrderByPriceAscTest() {
        //given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Match match = new Match(createMember(), Category.반려동물, "러시안 블루 밥주기", "경기 시흥시", LocalDateTime.parse("2023-04-15 10:00", formatter), LocalDateTime.parse("2023-04-15 11:00", formatter), "동물 돌보미", 50000, "사나우니까 조심좀해주세요", false);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("price").ascending());
        List<Match> matches = List.of(match, createMatch());
        given(matchRepository.findAllByOrderByPrice(pageRequest)).willReturn(new PageImpl<>(matches, pageRequest, matches.size()));

        //when
        MatchFindAllWithPagingResponseDto result = matchService.getAllMatchesPostWithPagingOrderByPriceAsc(0);

        //then
        assertThat(result.getMatches().get(0).getPrice()).isEqualTo(50000);
    }

    @DisplayName("매칭글 조회 테스트(가격 내림차순)")
    @Test
    void getAllMatchesPostWithPagingOrderByPriceDescTest() {
        //given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Match match1 = new Match(createMember(), Category.반려동물, "러시안 블루 밥주기", "경기 시흥시", LocalDateTime.parse("2023-04-15 10:00", formatter), LocalDateTime.parse("2023-04-15 10:00", formatter), "동물 돌보미", 50000, "사나우니까 조심좀해주세요", false);
        Match match2 = new Match(createMember(), Category.노인돌봄, "노인 책 읽어주기", "제주도", LocalDateTime.parse("2023-05-04 10:00", formatter), LocalDateTime.parse("2023-05-04 11:00", formatter), "노인 돌보미", 150000, "거동이 불편합니다", false);
        List<Match> matches = List.of(match1, match2);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("price").descending());
        given(matchRepository.findAllByOrderByPriceDesc(pageRequest)).willReturn(new PageImpl<>(matches, pageRequest, matches.size()));

        //when
        MatchFindAllWithPagingResponseDto result = matchService.getAllMatchesPostWithPagingOrderByPriceDesc(0);

        //then
        assertThat(result.getMatches().get(0).getPrice()).isEqualTo(50000);
    }

    @DisplayName("매칭글 검색 조회 테스트(주소 및 매칭글 제목으로 검색)")
    @Test
    void getAllMatchesPostWithPagingBySearchingAddressTest() {
        String keyword = "시흥";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Match match1 = new Match(createMember(), Category.반려동물, "러시안 블루 밥주기", "경기 시흥시", LocalDateTime.parse("2023-04-15 10:00", formatter), LocalDateTime.parse("2023-04-15 10:00", formatter), "동물 돌보미", 50000, "사나우니까 조심좀해주세요", false);
        Match match2 = new Match(createMember(), Category.노인돌봄, "노인 책 읽어주기", "제주도", LocalDateTime.parse("2023-05-04 10:00", formatter), LocalDateTime.parse("2023-05-04 11:00", formatter), "노인 돌보미", 150000, "거동이 불편합니다", false);
        List<Match> matches = List.of(match1);

        given(matchRepository.findAllByMatchNameContainingOrAddressContaining(keyword, keyword, PageRequest.of(0, 10, Sort.by("id").descending()))).willReturn(new PageImpl<>(
            matches));

        //when
        MatchFindAllWithPagingResponseDto result = matchService.getAllMatchesPostWithPagingBySearchingMatchNameOrAddress(keyword, 0);

        //then
        assertThat(result.getMatches().size()).isEqualTo(1);
        assertThat(result.getMatches().get(0).getAddress()).isEqualTo(match1.getAddress());
    }

    @DisplayName("매칭글 즐겨찾기 테스트")
    @Test
    void createFavoriteMatchPostTest() {
        //given
        Member member = createMember();
        Match match = createMatch();

        given(matchRepository.findById(1L)).willReturn(Optional.of(match));
        given(matchFavoriteRepository.existsByMatchAndMember(match, member)).willReturn(false);

        //when
        String result = matchService.updateMatchFavoritePost(1L, member);

        //then
        assertThat(result).isEqualTo("즐겨찾기를 하였습니다.");
        verify(matchFavoriteRepository).save(any());
    }

    @DisplayName("매칭글 즐겨찾기 취소 테스트")
    @Test
    void deleteMatchFavoritePostTest() {
        //given
        Member member = createMember();
        Match match = createMatch();
        MatchFavorite matchFavorite = MatchFavorite.createFavorite(match, member);

        given(matchRepository.findById(1L)).willReturn(Optional.of(match));
        given(matchFavoriteRepository.existsByMatchAndMember(match, member)).willReturn(true);
        given(matchFavoriteRepository.findByMatchAndMember(match, member)).willReturn(Optional.of(matchFavorite));

        //when
        String result = matchService.updateMatchFavoritePost(1L, member);

        //then
        assertThat(result).isEqualTo("즐겨찾기가 해제되었습니다.");
        verify(matchFavoriteRepository).delete(any());
    }

    @DisplayName("매칭글 전체목록 조회 테스트")
    @Test
    void findFavoriteMatchesPostsTest() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id").descending());
        Member member = createMember();
        List<MatchFavorite> favorites = new ArrayList<>();
        favorites.add(MatchFavorite.createFavorite(createMatch(member), member));
        Page<MatchFavorite> favoritesWithPaging = new PageImpl<>(favorites);
        given(matchFavoriteRepository.findAllByMember(member, pageRequest)).willReturn(favoritesWithPaging);

        //when
        MatchFindAllWithPagingResponseDto result = matchService.findFavoriteMatchesPosts(0, member);

        //then
        assertThat(result.getMatches().size()).isEqualTo(1);

    }

    @DisplayName("매칭글 상태변경 테스트")
    @Test
    void changeMatchStatusTest() {
        //given
        Member member = createMember();
        Match match = createMatch(member);
        given(matchRepository.findById(1L)).willReturn(Optional.of(match));

        //when
        String result = matchService.changeMatchStatus(member, 1L);

        //then
        assertThat(result).isEqualTo("매칭글 상태를 변경하였습니다.");
        assertThat(match.isMatched()).isTrue();
    }
}
