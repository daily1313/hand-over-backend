package com.example.handoverbackend.dto.ticket;

import com.example.handoverbackend.domain.ticket.Category;
import com.example.handoverbackend.domain.ticket.Ticket;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDto {

    private Category category;
    private String ticketName;
    private String sellerNickname;
    private String address;
    private LocalDate startDate;
    private LocalDate endDate;
    private String detailsContent;
    private int price;

    public static TicketResponseDto toDto(Ticket ticket) {
        return new TicketResponseDto(
                ticket.getCategory(),
                ticket.getTicketName(),
                ticket.getSeller().getNickname(),
                ticket.getAddress(),
                ticket.getStartDate(),
                ticket.getEndDate(),
                ticket.getDetailsContent(),
                ticket.getPrice()
        );
    }
}
