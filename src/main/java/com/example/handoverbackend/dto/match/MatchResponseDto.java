package com.example.handoverbackend.dto.match;

import com.example.handoverbackend.domain.match.Category;
import com.example.handoverbackend.domain.match.Match;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MatchResponseDto {

    private Long id;
    private Category category;
    private String matchName;
    private String sellerNickname;
    private String address;
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startDate;
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endDate;
    private String detailsContent;
    private int price;
    private String precaution;

    public static MatchResponseDto toDto(Match match) {
        return new MatchResponseDto(
                match.getId(),
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
