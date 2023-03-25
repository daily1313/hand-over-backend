package com.example.handoverbackend.dto.board;

import com.example.handoverbackend.domain.board.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardFindAllResponseDto {
    private Long id;
    private String title;
    private String nickname;
    private LocalDateTime createAt;

    public static BoardFindAllResponseDto toDto(Board board){
        return new BoardFindAllResponseDto(board.getId(), board.getTitle(), board.getMember().getNickname(), board.getCreatedAt());
    }
}
