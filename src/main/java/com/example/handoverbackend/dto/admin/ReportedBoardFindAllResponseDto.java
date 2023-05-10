package com.example.handoverbackend.dto.admin;

import com.example.handoverbackend.domain.board.Board;
import com.example.handoverbackend.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportedBoardFindAllResponseDto {

    private Long id;
    private String title;
    private String username;
    private LocalDateTime signUpDate;
    private boolean reportedStatus;
    private int reportedCount;

    public static ReportedBoardFindAllResponseDto toDto(Board board, Member member, int reportedCount) {
        return new ReportedBoardFindAllResponseDto(board.getId(), board.getTitle(), member.getUsername(),
            member.getCreatedAt(), member.isReportedStatus(), reportedCount);
    }
}
