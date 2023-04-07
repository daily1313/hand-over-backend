package com.example.handoverbackend.dto.ticket;

import com.example.handoverbackend.dto.page.PageInfoDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TicketFindAllWithPagingResponseDto {

    private List<TicketResponseDto> tickets;
    private PageInfoDto pageInfoDto;

    public static TicketFindAllWithPagingResponseDto toDto(List<TicketResponseDto> tickets, PageInfoDto pageInfoDto) {
        return new TicketFindAllWithPagingResponseDto(tickets, pageInfoDto);
    }
}
