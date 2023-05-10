package com.example.handoverbackend.dto.admin;

import com.example.handoverbackend.domain.match.Match;
import com.example.handoverbackend.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportedMatchFindAllResponseDto {

    private Long id;
    private String title;
    private String username;
    private LocalDateTime createDate;
    private boolean reportedStatus;
    private int reportedCount;

    public static ReportedMatchFindAllResponseDto toDto(Match match, Member member, int reportedCount) {
        return new ReportedMatchFindAllResponseDto(match.getId(), match.getMatchName(),
            member.getUsername(), match.getCreatedAt(), match.isReportedStatus(), reportedCount);
    }
}
