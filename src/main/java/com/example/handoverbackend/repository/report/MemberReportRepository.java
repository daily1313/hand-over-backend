package com.example.handoverbackend.repository.report;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.report.MemberReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberReportRepository extends JpaRepository<MemberReport, Long> {

    boolean existsByReporterAndReported(Member reporter, Member reported);

    List<MemberReport> findAllByReported(Member reported);

    void deleteAllByReported(Member member);
}
