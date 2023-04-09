package com.example.handoverbackend.dto.comment;

import com.example.handoverbackend.dto.page.PageInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentFindAllWithPagingResponseDto {

    private List<CommentResponseDto> comments;
    private PageInfoDto pageInfoDto;

   public static CommentFindAllWithPagingResponseDto toDto(List<CommentResponseDto> comments, PageInfoDto pageInfoDto) {
       return new CommentFindAllWithPagingResponseDto(comments, pageInfoDto);
   }
}
