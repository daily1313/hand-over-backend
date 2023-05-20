package com.example.handoverbackend.dto.comment;

import com.example.handoverbackend.domain.comment.MatchComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchCommentResponseDto {

    private Long id;
    private String writer;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static MatchCommentResponseDto toDto(MatchComment comment) {
        return new MatchCommentResponseDto(comment.getId(), comment.getWriter().getNickname(),
            comment.getContent(), comment.getCreatedAt(), comment.getModifiedAt());
    }
}
