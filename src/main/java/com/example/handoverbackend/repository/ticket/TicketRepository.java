package com.example.handoverbackend.repository.ticket;

import com.example.handoverbackend.domain.ticket.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findAllByOrderByStartDate(Pageable pageable);
    Page<Ticket> findAllByOrderByPrice(Pageable pageable);
    Page<Ticket> findAllByTicketNameContaining(String keyword, Pageable pageable);
    Page<Ticket> findAllByAddressContaining(String keyword, Pageable pageable);

}
