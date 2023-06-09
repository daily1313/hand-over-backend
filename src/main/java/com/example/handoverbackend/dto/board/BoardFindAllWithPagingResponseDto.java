package com.example.handoverbackend.dto.board;


import com.example.handoverbackend.dto.page.PageInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoardFindAllWithPagingResponseDto {

    private List<BoardFindAllResponseDto> boards;
    private PageInfoDto pageInfoDto;

    public static BoardFindAllWithPagingResponseDto toDto(List<BoardFindAllResponseDto> boards, PageInfoDto pageInfoDto) {
        return new BoardFindAllWithPagingResponseDto(boards, pageInfoDto);
    }
}
