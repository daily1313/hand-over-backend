package com.example.handoverbackend.service.admin;

import com.example.handoverbackend.domain.board.Board;
import com.example.handoverbackend.domain.match.Match;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.admin.ReportedBoardFindAllResponseDto;
import com.example.handoverbackend.dto.admin.ReportedMatchFindAllResponseDto;
import com.example.handoverbackend.dto.admin.ReportedMemberFindAllResponseDto;
import com.example.handoverbackend.exception.*;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.repository.board.BoardRepository;
import com.example.handoverbackend.repository.match.MatchRepository;
import com.example.handoverbackend.repository.report.BoardReportRepository;
import com.example.handoverbackend.repository.report.MatchReportRepository;
import com.example.handoverbackend.repository.report.MemberReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminService {

    private static final String UNLOCK = "신고가 해제되었습니다.";
    private static final String SUSPEND = "신고가 되었습니다.";
    private static final String DELETE = "삭제가 되었습니다.";

    private final MemberRepository memberRepository;
    private final MemberReportRepository memberReportRepository;
    private final BoardRepository boardRepository;
    private final BoardReportRepository boardReportRepository;
    private final MatchRepository matchRepository;
    private final MatchReportRepository matchReportRepository;

    @Transactional(readOnly = true)
    public List<ReportedMemberFindAllResponseDto> findAllReportedMember() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
            .map(i -> ReportedMemberFindAllResponseDto.toDto(i, getReportedCount(i)))
            .collect(Collectors.toList());
    }

    private int getReportedCount(Member member) {
        return memberReportRepository.findAllByReported(member).size();
    }

    @Transactional
    public String unlockMember(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(MemberNotFoundException::new);
        validateUnlockMember(member);
        unlockMember(member);
        return UNLOCK;
    }

    private void validateUnlockMember(Member member) {
        if (!member.isReportedStatus()) {
            throw new NotReportedException();
        }
    }

    private void unlockMember(Member member) {
        member.unLockSuspend();
        memberReportRepository.deleteAllByReported(member);
    }

    @Transactional
    public String lockMember(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(MemberNotFoundException::new);
        validateLockMember(member);
        lockMember(member);
        return SUSPEND;
    }

    private void validateLockMember(Member member) {
        if (member.isReportedStatus()) {
            throw new AlreadyReportException();
        }
    }

    private void lockMember(Member member) {
        member.suspend();
    }

    @Transactional
    public String deleteReportedMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        memberRepository.delete(member);
        return DELETE;
    }

    @Transactional(readOnly = true)
    public List<ReportedBoardFindAllResponseDto> findAllReportedBoards() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
            .map(b -> ReportedBoardFindAllResponseDto.toDto(b, b.getMember(), getReportedCount(b)))
            .collect(Collectors.toList());
    }

    private int getReportedCount(Board board) {
        return boardReportRepository.findAllByReported(board).size();
    }

    @Transactional
    public String unlockBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        validateUnlockBoard(board);
        unlockBoard(board);
        return UNLOCK;
    }

    private void validateUnlockBoard(Board board) {
        if (!board.isReportedStatus()) {
            throw new NotReportedException();
        }
    }

    private void unlockBoard(Board board) {
        board.unLockSuspend();
        boardReportRepository.deleteAllByReported(board);
    }

    @Transactional
    public String lockBoard(Long id) {
        Board board = boardRepository.findById(id)
            .orElseThrow(BoardNotFoundException::new);
        validateLockBoard(board);
        lockBoard(board);
        return SUSPEND;
    }

    private void validateLockBoard(Board board) {
        if (board.isReportedStatus()) {
            throw new AlreadyReportException();
        }
    }

    private void lockBoard(Board board) {
        board.suspend();
    }

    @Transactional
    public String deleteReportedBoard(Long id) {
        Board board = boardRepository.findById(id)
            .orElseThrow(BoardNotFoundException::new);
        boardRepository.delete(board);
        return DELETE;
    }

    @Transactional(readOnly = true)
    public List<ReportedMatchFindAllResponseDto> findAllReportedMatches() {
        List<Match> matches = matchRepository.findAll();
        return matches.stream()
            .map(m -> ReportedMatchFindAllResponseDto.toDto(m, m.getSeller(), getReportedCount(m)))
            .collect(Collectors.toList());
    }

    private int getReportedCount(Match match) {
        return matchReportRepository.findAllByReported(match).size();
    }

    @Transactional
    public String unlockMatch(Long id) {
        Match match = matchRepository.findById(id)
            .orElseThrow(MatchNotFoundException::new);
        validateUnlockMatch(match);
        unlockMatch(match);
        return UNLOCK;
    }

    private void validateUnlockMatch(Match match) {
        if (!match.isReportedStatus()) {
            throw new NotReportedException();
        }
    }

    private void unlockMatch(Match match) {
        match.unLockSuspend();
        matchReportRepository.deleteAllByReported(match);
    }

    @Transactional
    public String lockMatch(Long id) {
        Match match = matchRepository.findById(id)
            .orElseThrow(MatchNotFoundException::new);
        validateLockMatch(match);
        lockMatch(match);
        return SUSPEND;
    }

    private void validateLockMatch(Match match) {
        if (match.isReportedStatus()) {
            throw new AlreadyReportException();
        }
    }

    private void lockMatch(Match match) {
        match.suspend();
    }

    @Transactional
    public String deleteReportedMatch(Long id) {
        Match match = matchRepository.findById(id)
            .orElseThrow(MatchNotFoundException::new);
        matchRepository.delete(match);
        return DELETE;
    }
}
