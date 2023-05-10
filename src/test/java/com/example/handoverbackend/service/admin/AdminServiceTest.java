package com.example.handoverbackend.service.admin;

import com.example.handoverbackend.domain.board.Board;
import com.example.handoverbackend.domain.match.Match;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.admin.ReportedBoardFindAllResponseDto;
import com.example.handoverbackend.dto.admin.ReportedMatchFindAllResponseDto;
import com.example.handoverbackend.dto.admin.ReportedMemberFindAllResponseDto;
import com.example.handoverbackend.exception.AlreadyReportException;
import com.example.handoverbackend.exception.MemberNotFoundException;
import com.example.handoverbackend.exception.NotReportedException;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.repository.board.BoardRepository;
import com.example.handoverbackend.repository.match.MatchRepository;
import com.example.handoverbackend.repository.report.BoardReportRepository;
import com.example.handoverbackend.repository.report.MatchReportRepository;
import com.example.handoverbackend.repository.report.MemberReportRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.handoverbackend.factory.BoardMaker.createBoard;
import static com.example.handoverbackend.factory.MatchMaker.createMatch;
import static com.example.handoverbackend.factory.MemberMaker.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @InjectMocks
    AdminService adminService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    MemberReportRepository memberReportRepository;
    @Mock
    BoardRepository boardRepository;
    @Mock
    BoardReportRepository boardReportRepository;
    @Mock
    MatchRepository matchRepository;
    @Mock
    MatchReportRepository matchReportRepository;

    private static final String UNLOCK = "신고가 해제되었습니다.";
    private static final String SUSPEND = "신고가 되었습니다.";
    private static final String DELETE = "삭제가 되었습니다.";

    @Test
    @DisplayName("멤버 관리 테스트")
    void findAllReportedMember() {
        //given
        List<Member> members = new ArrayList<>();
        members.add(createMember());
        given(memberRepository.findAll()).willReturn(members);

        //when
        List<ReportedMemberFindAllResponseDto> result = adminService.findAllReportedMember();

        //then
        assertThat(result).size().isEqualTo(1);
    }

    @Test
    @DisplayName("멤버 정지 해제 테스트")
    void unlockMember() {
        //given
        Long id = 1L;
        Member member = createMember();
        member.suspend();
        given(memberRepository.findById(id)).willReturn(Optional.of(member));

        //when
        String result = adminService.unlockMember(id);

        //then
        assertThat(result).isEqualTo(UNLOCK);
    }

    @Test
    @DisplayName("멤버 정지 테스트")
    void lockMember() {
        //given
        Long id = 1L;
        Member member = createMember();
        given(memberRepository.findById(id)).willReturn(Optional.of(member));

        //when
        String result = adminService.lockMember(id);

        //then
        assertThat(result).isEqualTo(SUSPEND);
    }

    @Test
    @DisplayName("멤버 삭제 테스트")
    void deleteReportedMember() {
        //given
        Long id = 1L;
        Member member = createMember();
        given(memberRepository.findById(id)).willReturn(Optional.of(member));

        //when
        String result = adminService.deleteReportedMember(id);

        //then
        assertThat(result).isEqualTo(DELETE);
    }

    @Test
    @DisplayName("게시판 관리 테스트")
    void findAllReportedBoard() {
        //given
        List<Board> boards = new ArrayList<>();
        boards.add(createBoard());
        given(boardRepository.findAll()).willReturn(boards);

        //when
        List<ReportedBoardFindAllResponseDto> result = adminService.findAllReportedBoards();

        //then
        assertThat(result).size().isEqualTo(1);
    }

    @Test
    @DisplayName("게시판 정지 해제 테스트")
    void unlockBoard() {
        //given
        Long id = 1L;
        Board board = createBoard();
        board.suspend();
        given(boardRepository.findById(id)).willReturn(Optional.of(board));

        //when
        String result = adminService.unlockBoard(id);

        //then
        assertThat(result).isEqualTo(UNLOCK);
    }

    @Test
    @DisplayName("게시판 정지 테스트")
    void lockBoard() {
        //given
        Long id = 1L;
        Board board = createBoard();
        given(boardRepository.findById(id)).willReturn(Optional.of(board));

        //when
        String result = adminService.lockBoard(id);

        //then
        assertThat(result).isEqualTo(SUSPEND);
    }

    @Test
    @DisplayName("게시판 삭제 테스트")
    void deleteReportedBoard() {
        //given
        Long id = 1L;
        Board board = createBoard();
        given(boardRepository.findById(id)).willReturn(Optional.of(board));

        //when
        String result = adminService.deleteReportedBoard(id);

        //then
        assertThat(result).isEqualTo(DELETE);
    }

    @Test
    @DisplayName("매칭 관리 테스트")
    void findAllReportedMatch() {
        //given
        List<Match> matches = new ArrayList<>();
        matches.add(createMatch());
        given(matchRepository.findAll()).willReturn(matches);

        //when
        List<ReportedMatchFindAllResponseDto> result = adminService.findAllReportedMatches();

        //then
        assertThat(result).size().isEqualTo(1);
    }

    @Test
    @DisplayName("매칭 정지 해제 테스트")
    void unlockMatch() {
        //given
        Long id = 1L;
        Match match = createMatch();
        match.suspend();
        given(matchRepository.findById(id)).willReturn(Optional.of(match));

        //when
        String result = adminService.unlockMatch(id);

        //then
        assertThat(result).isEqualTo(UNLOCK);
    }

    @Test
    @DisplayName("매칭 정지 테스트")
    void lockMatch() {
        //given
        Long id = 1L;
        Match match = createMatch();
        given(matchRepository.findById(id)).willReturn(Optional.of(match));

        //when
        String result = adminService.lockMatch(id);

        //then
        assertThat(result).isEqualTo(SUSPEND);
    }

    @Test
    @DisplayName("매칭 삭제 테스트")
    void deleteReportedMatch() {
        //given
        Long id = 1L;
        Match match = createMatch();
        given(matchRepository.findById(id)).willReturn(Optional.of(match));

        //when
        String result = adminService.deleteReportedMatch(id);

        //then
        assertThat(result).isEqualTo(DELETE);
    }

    @Test
    @DisplayName("정지를 당한적이 없는데 정지를 해제하려하면 예외발생")
    void unlockException() {
        //given
        Long id = 1L;
        Member member = createMember();
        given(memberRepository.findById(id)).willReturn(Optional.of(member));

        //when, then
         assertThatThrownBy(() -> adminService.unlockMember(id))
             .isInstanceOf(NotReportedException.class);
    }

    @Test
    @DisplayName("정지를 시키려는데 이미 정지상태이면 예외발생")
    void lockException() {
        //given
        Long id = 1L;
        Member member = createMember();
        member.suspend();
        given(memberRepository.findById(id)).willReturn(Optional.of(member));

        //when, then
        assertThatThrownBy(() -> adminService.lockMember(id))
            .isInstanceOf(AlreadyReportException.class);
    }

    @Test
    @DisplayName("멤버가 존재하지않으면 예외발생")
    void memberNotExistExcetpion() {
        //given
        Long id = 1L;
        given(memberRepository.findById(id)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> adminService.unlockMember(id))
            .isInstanceOf(MemberNotFoundException.class);
    }
}
