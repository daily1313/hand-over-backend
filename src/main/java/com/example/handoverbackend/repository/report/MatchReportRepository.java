package com.example.handoverbackend.repository.report;

import com.example.handoverbackend.domain.match.Match;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.report.MatchReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchReportRepository extends JpaRepository<MatchReport, Long> {

    boolean existsByReporterAndReported(Member reporter, Match reported);

    List<MatchReport> findAllByReported(Match reported);

    void deleteAllByReported(Match match);
}
