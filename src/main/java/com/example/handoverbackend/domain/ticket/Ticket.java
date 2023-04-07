package com.example.handoverbackend.domain.ticket;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.ticket.TicketEditRequestDto;
import com.example.handoverbackend.dto.ticket.TicketResponseDto;
import com.example.handoverbackend.exception.AlreadyOnSaleException;
import com.example.handoverbackend.exception.AlreadySoldOutException;
import com.example.handoverbackend.exception.MemberNotEqualsException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member seller;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String ticketName;

    @Column(nullable = false)
    private String address;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    @Lob
    private String detailsContent;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private boolean isOnSale;

    @Builder
    public Ticket(Member seller, Category category, String ticketName, String address, LocalDate startDate, LocalDate endDate,
                  String detailsContent, int price, boolean isOnSale) {
        this.ticketName = ticketName;
        this.seller = seller;
        this.category = category;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
        this.detailsContent = detailsContent;
        this.price = price;
        this.isOnSale = true;
    }

    public static void validateSeller(Member loginUser, Member seller) {
        if(!loginUser.equals(seller)) {
            throw new MemberNotEqualsException();
        }
    }

    public static void validateIsSoldOut(Ticket ticket) {
        if(!ticket.isOnSale()) {
            throw new AlreadySoldOutException();
        }
    }

    public static void validateIsOnSale(Ticket ticket) {
        if(ticket.isOnSale()) {
            throw new AlreadyOnSaleException();
        }
    }

    public void changeSoldOutTicketStatus() {
        isOnSale = false;
    }

    public void changeOnSaleTicketStatus() {
        isOnSale = true;
    }

    public void editTicketInfo(TicketEditRequestDto req) {
        category = req.getCategory();
        ticketName = req.getTicketName();
        address = req.getAddress();
        startDate = req.getStartDate();
        endDate = req.getEndDate();
        detailsContent = req.getDetailsContent();
        price = req.getPrice();
    }
}
