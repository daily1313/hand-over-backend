package com.example.handoverbackend.dto.member;

import com.example.handoverbackend.domain.member.Authority;
import com.example.handoverbackend.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberResponseDto {

    private String name;
    private String username;
    private String nickname;
    private Authority authority;

    public static MemberResponseDto toDto(Member member) {
        return new MemberResponseDto(member.getName(), member.getUsername(), member.getNickname(), member.getAuthority());
    }

    public MemberResponseDto(String name, String username, String nickname, Authority authority) {
        this.name = name;
        this.username = username;
        this.nickname = nickname;
        this.authority = authority;
    }
}
