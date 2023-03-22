package com.example.handoverbackend.dto.member;

import com.example.handoverbackend.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberEditRequestDto {

    private String username;
    private String password;
    private String nickname;

    public static MemberFindRequestDto toDto(Member member) {
        return new MemberFindRequestDto(member.getUsername(), member.getPassword(), member.getNickname());
    }
}
