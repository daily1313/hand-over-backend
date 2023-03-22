package com.example.handoverbackend.dto.member;

import com.example.handoverbackend.domain.member.Authority;
import com.example.handoverbackend.domain.member.Member;
import com.querydsl.core.annotations.QueryProjection;
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

    @QueryProjection
    public MemberResponseDto(String name, String username, String nickname, Authority authority) {
        this.name = name;
        this.username = username;
        this.nickname = nickname;
        this.authority = authority;
    }
}
