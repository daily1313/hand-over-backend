package com.example.handoverbackend.repository.ticket;

import com.example.handoverbackend.domain.favorite.TicketFavorite;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.ticket.Ticket;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketFavoriteRepository extends JpaRepository<TicketFavorite, Long> {

    Page<TicketFavorite> findAllByMember(Member member, Pageable pageable);

    Optional<TicketFavorite> findByTicketAndMember(Ticket ticket, Member member);

    boolean existsByTicketAndMember(Ticket ticket, Member member);
}
