package com.example.handoverbackend.factory;

import com.example.handoverbackend.domain.report.BoardReport;
import com.example.handoverbackend.domain.report.MemberReport;

import java.util.ArrayList;
import java.util.List;

import static com.example.handoverbackend.factory.BoardMaker.createBoard;
import static com.example.handoverbackend.factory.MemberMaker.createMember;
import static com.example.handoverbackend.factory.MemberMaker.createMember2;

public class ReportMaker {

    public static List<MemberReport> createMemberReports() {
        List<MemberReport> memberReports = new ArrayList<>();
        memberReports.add(new MemberReport(createMember(), createMember2(), "report"));
        memberReports.add(new MemberReport(createMember(), createMember2(), "report"));
        memberReports.add(new MemberReport(createMember(), createMember2(), "report"));
        memberReports.add(new MemberReport(createMember(), createMember2(), "report"));
        memberReports.add(new MemberReport(createMember(), createMember2(), "report"));
        return memberReports;
    }

    public static List<BoardReport> createBoardReports() {
        List<BoardReport> boardReports = new ArrayList<>();
        boardReports.add(new BoardReport(createMember(), createBoard(), "report"));
        boardReports.add(new BoardReport(createMember(), createBoard(), "report"));
        boardReports.add(new BoardReport(createMember(), createBoard(), "report"));
        boardReports.add(new BoardReport(createMember(), createBoard(), "report"));
        boardReports.add(new BoardReport(createMember(), createBoard(), "report"));
        return boardReports;
    }
}
