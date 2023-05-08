package com.example.handoverbackend.dto.comment;

import com.example.handoverbackend.dto.page.PageInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MatchCommentFindAllWithPagingResponseDto {

    private List<MatchCommentResponseDto> comments;
    private PageInfoDto pageInfoDto;

   public static MatchCommentFindAllWithPagingResponseDto toDto(List<MatchCommentResponseDto> comments, PageInfoDto pageInfoDto) {
       return new MatchCommentFindAllWithPagingResponseDto(comments, pageInfoDto);
   }
}
