package com.example.handoverbackend.dto.match;

import com.example.handoverbackend.domain.match.Match;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchByMemberResponseDto {

    private Long id;
    private String title;
    private boolean isMatched;

    public static MatchByMemberResponseDto toDto(Match match) {
        return new MatchByMemberResponseDto(match.getId(), match.getMatchName(), match.isMatched());
    }
}
