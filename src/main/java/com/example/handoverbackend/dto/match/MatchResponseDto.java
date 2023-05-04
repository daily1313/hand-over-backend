package com.example.handoverbackend.dto.match;

import com.example.handoverbackend.domain.match.Category;
import com.example.handoverbackend.domain.match.Match;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MatchResponseDto {

    private Category category;
    private String ticketName;
    private String sellerNickname;
    private String address;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String detailsContent;
    private int price;
    private String precaution;

    public static MatchResponseDto toDto(Match match) {
        return new MatchResponseDto(
                match.getCategory(),
                match.getMatchName(),
                match.getSeller().getNickname(),
                match.getAddress(),
                match.getStartDate(),
                match.getEndDate(),
                match.getDetailsContent(),
                match.getPrice(),
                match.getPrecaution()
        );
    }
}
