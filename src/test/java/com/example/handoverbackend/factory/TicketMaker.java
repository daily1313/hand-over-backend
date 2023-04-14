package com.example.handoverbackend.factory;

import static com.example.handoverbackend.factory.MemberMaker.createMember;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.ticket.Category;
import com.example.handoverbackend.domain.ticket.Ticket;
import java.time.LocalDate;

public class TicketMaker {

    public static Ticket createTicket() {
        return new Ticket(createMember(), Category.공연, "아이유 콘서트", "경기 용인시", LocalDate.parse("2023-04-12"), LocalDate.parse("2023-04-12"), "공연", 150000, true);
    }

    public static Ticket createTicket(Member member) {
        return new Ticket(member, Category.공연, "아이유 콘서트", "경기 용인시", LocalDate.parse("2023-04-12"), LocalDate.parse("2023-04-12"), "공연", 150000, true);
    }
}
