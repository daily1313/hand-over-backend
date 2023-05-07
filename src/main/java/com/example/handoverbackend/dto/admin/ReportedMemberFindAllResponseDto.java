package com.example.handoverbackend.dto.admin;

import com.example.handoverbackend.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportedMemberFindAllResponseDto {

    private Long id;
    private String username;
    private String email;
    private LocalDateTime signUpDate;
    private boolean reportedStatus;
    private int reportedCount;

    public static ReportedMemberFindAllResponseDto toDto(Member member, int reportedCount) {
        return new ReportedMemberFindAllResponseDto(member.getId(), member.getUsername()
            , member.getEmail(), member.getCreatedAt(), member.isReportedStatus(), reportedCount);
    }
}
