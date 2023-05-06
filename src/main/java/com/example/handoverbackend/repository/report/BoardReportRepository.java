package com.example.handoverbackend.repository.report;

import com.example.handoverbackend.domain.board.Board;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.report.BoardReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardReportRepository extends JpaRepository<BoardReport, Long> {

    boolean existsByReporterAndReported(Member reporter, Board reported);

    List<BoardReport> findAllByReported(Board reported);

    void deleteAllByReported(Board board);
}
