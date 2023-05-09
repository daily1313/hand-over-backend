package com.example.handoverbackend.domain.member;

import com.example.handoverbackend.domain.common.BaseEntity;
import com.example.handoverbackend.dto.member.MemberEditRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Builder
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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(nullable = true)
    private String provider;

    @Column(nullable = true)
    private String providerId;

    @Column(nullable = false)
    private boolean reportedStatus;

    public List<String> getRoleList() {
        if(this.authority.name().length()> 0) {
            return Arrays.asList(this.authority.name());
        }
        return new ArrayList<>();
    }

    public Member(String username, String password, String name, String email, String nickname, Authority authority) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.authority = authority;
        reportedStatus = false;
    }

    public static Member createMember(String username, String password, String name, String email, String nickname, Authority authority) {
        return new Member(username, password, name, email, nickname, authority);
    }

    public void editMember(MemberEditRequestDto req) {
        username = req.getUsername();
        password = req.getPassword();
        nickname = req.getNickname();
    }

    public void suspend() {
        reportedStatus = true;
        authority = Authority.ROLE_SUSPEND;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Authority authority) {
        this.authority = authority;
    }

    public void unLockSuspend() {
        reportedStatus = false;
        authority = Authority.ROLE_USER;
    }

    public Member update(String name) {
        this.name = name;
        return this;
    }

    public String getNickname() {
        return nickname;
    }
}
