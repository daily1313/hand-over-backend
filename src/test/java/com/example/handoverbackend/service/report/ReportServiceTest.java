package com.example.handoverbackend.service.report;

import com.example.handoverbackend.domain.board.Board;
import com.example.handoverbackend.domain.match.Match;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.report.BoardReport;
import com.example.handoverbackend.domain.report.MemberReport;
import com.example.handoverbackend.dto.report.BoardReportRequestDto;
import com.example.handoverbackend.dto.report.MatchReportRequestDto;
import com.example.handoverbackend.dto.report.MemberReportRequestDto;
import com.example.handoverbackend.exception.AlreadyReportException;
import com.example.handoverbackend.exception.BoardNotFoundException;
import com.example.handoverbackend.exception.MemberNotFoundException;
import com.example.handoverbackend.exception.NotSelfReportException;
import com.example.handoverbackend.factory.BoardMaker;
import com.example.handoverbackend.repository.member.MemberRepository;
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

import java.util.List;
import java.util.Optional;

import static com.example.handoverbackend.factory.BoardMaker.createBoard;
import static com.example.handoverbackend.factory.MatchMaker.createMatch;
import static com.example.handoverbackend.factory.MemberMaker.createMember;
import static com.example.handoverbackend.factory.MemberMaker.createMember2;
import static com.example.handoverbackend.factory.ReportMaker.createBoardReports;
import static com.example.handoverbackend.factory.ReportMaker.createMemberReports;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @InjectMocks
    ReportService reportService;
    @Mock
    BoardReportRepository boardReportRepository;
    @Mock
    MemberReportRepository memberReportRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    BoardRepository boardRepository;
    @Mock
    MatchRepository matchRepository;
    @Mock
    MatchReportRepository matchReportRepository;

    private static final String SUCCESS_REPORT = "신고를 하였습니다.";

    @Test
    @DisplayName("유저 신고")
    public void reportMember() {
        //given
        Member reporter = createMember();
        Member reported = createMember2();
        MemberReportRequestDto requestDto = new MemberReportRequestDto(1l, "report");
        given(memberRepository.findById(1L)).willReturn(Optional.of(reported));
        given(memberReportRepository.existsByReporterAndReported(reporter, reported)).willReturn(false);
        given(memberReportRepository.findAllByReported(reported)).willReturn(List.of());

        //when
        String result = reportService.reportMember(requestDto, reporter);

        //then
        assertThat(result).isEqualTo(SUCCESS_REPORT);
    }

    @Test
    @DisplayName("게시글 신고")
    void reportBoard() {
        //given
        Member reporter = createMember();
        Board reported = createBoard();
        BoardReportRequestDto requestDto = new BoardReportRequestDto(1l, "report");
        given(boardRepository.findById(1L)).willReturn(Optional.of(reported));
        given(boardReportRepository.existsByReporterAndReported(reporter, reported)).willReturn(false);
        given(boardReportRepository.findAllByReported(reported)).willReturn(List.of());

        //when
        String result = reportService.reportBoard(requestDto, reporter);

        //then
        assertThat(result).isEqualTo(SUCCESS_REPORT);
    }

    @Test
    @DisplayName("매칭 신고")
    void reportMatch() {
        //given
        Member reporter = createMember();
        Match reported = createMatch();
        MatchReportRequestDto requestDto = new MatchReportRequestDto(1L, "내용");
        given(matchRepository.findById(1L)).willReturn(Optional.of(reported));
        given(matchReportRepository.existsByReporterAndReported(reporter, reported)).willReturn(false);
        given(matchReportRepository.findAllByReported(reported)).willReturn(List.of());

        //when
        String result = reportService.reportMatch(requestDto, reporter);

        //then
        assertThat(result).isEqualTo(SUCCESS_REPORT);
    }

    @Test
    @DisplayName("본인을 신고시 예외 발생")
    void reportMemberException() {
        //given
        Member reporter = createMember();
        MemberReportRequestDto requestDto = new MemberReportRequestDto(1l, "report");
        given(memberRepository.findById(1L)).willReturn(Optional.of(reporter));

        //when, then
        assertThatThrownBy(() -> reportService.reportMember(requestDto, reporter))
            .isInstanceOf(NotSelfReportException.class);
    }

    @Test
    @DisplayName("본인의 게시판을 신고시 예외 발생")
    void reportBoardException() {
        //given
        Member reporter = createMember();
        Board reported = BoardMaker.createBoard(reporter);
        BoardReportRequestDto requestDto = new BoardReportRequestDto(1l, "report");
        given(boardRepository.findById(1L)).willReturn(Optional.of(reported));

        //when, then
        assertThatThrownBy(() -> reportService.reportBoard(requestDto, reporter))
            .isInstanceOf(NotSelfReportException.class);
    }

    @Test
    @DisplayName("해당 유저가 없을시 예외 발생")
    void findMemberException() {
        //given
        Member reporter = createMember();
        MemberReportRequestDto requestDto = new MemberReportRequestDto(1l, "report");
        given(memberRepository.findById(1L)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> reportService.reportMember(requestDto, reporter))
            .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("해당 게시글이 없을시 예외 발생")
    void findBoardException() {
        //given
        Member reporter = createMember();
        BoardReportRequestDto requestDto = new BoardReportRequestDto(1l, "report");
        given(boardRepository.findById(1L)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> reportService.reportBoard(requestDto, reporter))
            .isInstanceOf(BoardNotFoundException.class);
    }

    @Test
    @DisplayName("이미 해당 유저를 신고했을 경우 예외 발생")
    public void alreadyReportMemberException() {
        //given
        Member reporter = createMember();
        Member reported = createMember2();
        MemberReportRequestDto requestDto = new MemberReportRequestDto(1l, "report");
        given(memberRepository.findById(1L)).willReturn(Optional.of(reported));
        given(memberReportRepository.existsByReporterAndReported(reporter, reported)).willReturn(true);

        //when, then
        assertThatThrownBy(() -> reportService.reportMember(requestDto, reporter))
            .isInstanceOf(AlreadyReportException.class);
    }

    @Test
    @DisplayName("이미 해당 게시글을 신고했을 경우 예외 발생")
    void alreadyReportBoardException() {
        //given
        Member reporter = createMember();
        Board reported = createBoard();
        BoardReportRequestDto requestDto = new BoardReportRequestDto(1l, "report");
        given(boardRepository.findById(1L)).willReturn(Optional.of(reported));
        given(boardReportRepository.existsByReporterAndReported(reporter, reported)).willReturn(true);

        //when, then
        assertThatThrownBy(() -> reportService.reportBoard(requestDto, reporter))
            .isInstanceOf(AlreadyReportException.class);
    }

    @Test
    @DisplayName("해당 유저의 누적 신고가 5회 이상일 경우 정지")
    void cumulativeReportMember() {
        //given
        Member reporter = createMember();
        Member reported = createMember2();
        List<MemberReport> reportHistory = createMemberReports();
        MemberReportRequestDto requestDto = new MemberReportRequestDto(1l, "report");
        given(memberRepository.findById(1L)).willReturn(Optional.of(reported));
        given(memberReportRepository.existsByReporterAndReported(reporter, reported)).willReturn(false);
        given(memberReportRepository.findAllByReported(reported)).willReturn(reportHistory);

        //when
        reportService.reportMember(requestDto, reporter);

        //then
        assertThat(reported.isReportedStatus()).isTrue();
    }

    @Test
    @DisplayName("해당 게시글의 누적 신고가 5회 이상일 경우 정지")
    void cumulativeReportBoard() {
        //given
        Member reporter = createMember();
        Board reported = createBoard();
        List<BoardReport> reportHistory = createBoardReports();
        BoardReportRequestDto requestDto = new BoardReportRequestDto(1l, "report");
        given(boardRepository.findById(1L)).willReturn(Optional.of(reported));
        given(boardReportRepository.existsByReporterAndReported(reporter, reported)).willReturn(false);
        given(boardReportRepository.findAllByReported(reported)).willReturn(reportHistory);

        //when
        reportService.reportBoard(requestDto, reporter);

        //then
        assertThat(reported.isReportedStatus()).isTrue();
    }
}
