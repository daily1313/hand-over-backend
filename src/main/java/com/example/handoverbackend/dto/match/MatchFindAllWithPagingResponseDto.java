package com.example.handoverbackend.dto.match;

import com.example.handoverbackend.dto.page.PageInfoDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchFindAllWithPagingResponseDto {

    private List<MatchResponseDto> matches;
    private PageInfoDto pageInfoDto;

    public static MatchFindAllWithPagingResponseDto toDto(List<MatchResponseDto> matches, PageInfoDto pageInfoDto) {
        return new MatchFindAllWithPagingResponseDto(matches, pageInfoDto);
    }
}
