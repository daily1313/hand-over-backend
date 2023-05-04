package com.example.handoverbackend.factory;

import static com.example.handoverbackend.factory.MemberMaker.createMember;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.match.Category;
import com.example.handoverbackend.domain.match.Match;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MatchMaker {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static Match createMatch() {
        return new Match(createMember(), Category.노인돌봄, "노인 돌봄", "경기 용인시", LocalDateTime.parse("2023-04-12 11:00", formatter), LocalDateTime.parse("2023-04-12 13:00", formatter), "돌보미", 150000, "조심하세요", false);
    }

    public static Match createMatch(Member member) {
        return new Match(member, Category.노인돌봄, "노인 돌봄", "경기 용인시", LocalDateTime.parse("2023-04-12 11:00", formatter), LocalDateTime.parse("2023-04-12 13:00", formatter), "돌보미", 150000, "조심하세요", false);
    }
}
