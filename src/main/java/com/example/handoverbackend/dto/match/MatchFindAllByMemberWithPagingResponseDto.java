package com.example.handoverbackend.dto.match;

import com.example.handoverbackend.dto.page.PageInfoDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchFindAllByMemberWithPagingResponseDto {

    private List<MatchByMemberResponseDto> matches;
    private PageInfoDto pageInfoDto;

    public static MatchFindAllByMemberWithPagingResponseDto toDto(List<MatchByMemberResponseDto> matches, PageInfoDto pageInfoDto) {
        return new MatchFindAllByMemberWithPagingResponseDto(matches, pageInfoDto);
    }
}
