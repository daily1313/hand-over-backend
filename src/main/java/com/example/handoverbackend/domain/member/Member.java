package com.example.handoverbackend.domain.member;

import com.example.handoverbackend.domain.common.BaseEntity;
import com.example.handoverbackend.dto.member.MemberEditRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(nullable = false)
    private boolean reportedStatus;

    public Member(String username, String password, String email, String nickname, Authority authority) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.authority = authority;
        reportedStatus = false;
    }

    public static Member createMember(String username, String password, String email, String nickname, Authority authority) {
        return new Member(username, password, email, nickname, authority);
    }

    public void editMember(MemberEditRequestDto req) {
        nickname = req.getNickname();
        password = req.getPassword();
    }

    public void suspend() {
        reportedStatus = true;
        authority = Authority.ROLE_SUSPEND;
    }

    public void unLockSuspend() {
        reportedStatus = false;
        authority = Authority.ROLE_USER;
    }

    public String getNickname() {
        return nickname;
    }
}
