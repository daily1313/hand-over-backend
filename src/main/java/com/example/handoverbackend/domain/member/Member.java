package com.example.handoverbackend.domain.member;

import com.example.handoverbackend.domain.common.BaseEntity;
import com.example.handoverbackend.dto.member.MemberEditRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Authority authority;



    public Member(String username, String password, String name, String email, String nickname, Authority authority) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.authority = authority;
    }

    public static Member createMember(String username, String password, String name, String email, String nickname, Authority authority) {
        return new Member(username, password, name, nickname , email, authority);
    }


    public void editMember(MemberEditRequestDto req) {
        username = req.getUsername();
        password = req.getPassword();
        nickname = req.getNickname();
    }
}
