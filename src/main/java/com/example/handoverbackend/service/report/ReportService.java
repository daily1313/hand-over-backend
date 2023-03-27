package com.example.handoverbackend.service.report;

import com.example.handoverbackend.domain.board.Board;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.report.BoardReport;
import com.example.handoverbackend.domain.report.MemberReport;
import com.example.handoverbackend.dto.report.BoardReportRequestDto;
import com.example.handoverbackend.dto.report.MemberReportRequestDto;
import com.example.handoverbackend.exception.AlreadyReportException;
import com.example.handoverbackend.exception.BoardNotFoundException;
import com.example.handoverbackend.exception.MemberNotFoundException;
import com.example.handoverbackend.exception.NotSelfReportException;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.repository.board.BoardRepository;
import com.example.handoverbackend.repository.report.BoardReportRepository;
import com.example.handoverbackend.repository.report.MemberReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private static final String SUCCESS_REPORT = "신고를 하였습니다.";

    private final BoardReportRepository boardReportRepository;
    private final MemberReportRepository memberReportRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public String reportMember(MemberReportRequestDto requestDto, Member reporter) {
        Member reportedMember = findReportMember(requestDto.getReportedMemberId());
        validationSelf(reportedMember, reporter);
        checkReportHistory(reportedMember, reporter);
        MemberReport memberReport = new MemberReport(reporter, reportedMember, requestDto.getContent());
        memberReportRepository.save(memberReport);
        checkCumulativeReport(reportedMember);
        return SUCCESS_REPORT;
    }

    @Transactional
    public String reportBoard(BoardReportRequestDto requestDto, Member reporter) {
        Board reportedBoard = findReportBoard(requestDto.getReportedBoardId());
        validationSelf(reportedBoard.getMember(), reporter);
        checkReportHistory(reportedBoard, reporter);
        BoardReport boardReport = new BoardReport(reporter, reportedBoard, requestDto.getContent());
        boardReportRepository.save(boardReport);
        checkCumulativeReport(reportedBoard);
        return SUCCESS_REPORT;
    }

    private Member findReportMember(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(MemberNotFoundException::new);
    }

    private Board findReportBoard(Long id) {
        return boardRepository.findById(id)
            .orElseThrow(BoardNotFoundException::new);
    }

    private void validationSelf(Member reportedMember, Member reporter) {
        if (reportedMember.equals(reporter)) {
            throw new NotSelfReportException();
        }
    }

    private void checkReportHistory(Member reportedMember, Member reporter) {
        if (memberReportRepository.existsByReporterAndReported(reporter, reportedMember)) {
            throw new AlreadyReportException();
        }
    }

    private void checkReportHistory(Board reportedBoard, Member reporter) {
        if (boardReportRepository.existsByReporterAndReported(reporter, reportedBoard)) {
            throw new AlreadyReportException();
        }
    }

    private void checkCumulativeReport(Member reportedMember) {
        if (memberReportRepository.findAllByReported(reportedMember).size() >= 5) {
            reportedMember.suspend();
        }
    }

    private void checkCumulativeReport(Board reportedBoard) {
        if (boardReportRepository.findAllByReported(reportedBoard).size() >= 5) {
            reportedBoard.suspend();
        }
    }
}
