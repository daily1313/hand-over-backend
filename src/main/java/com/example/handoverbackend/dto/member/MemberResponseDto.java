package com.example.handoverbackend.dto.member;

import com.example.handoverbackend.domain.member.Authority;
import com.example.handoverbackend.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {

    private Long id;
    private String username;
    private String nickname;
    private Authority authority;

    public static MemberResponseDto toDto(Member member) {
        return new MemberResponseDto(member.getId(), member.getUsername(), member.getNickname(), member.getAuthority());
    }
}
