package com.example.handoverbackend.factory;

import com.example.handoverbackend.domain.member.Authority;
import com.example.handoverbackend.domain.member.Member;

public class MemberMaker {

    public static Member createMember() {
        return Member.createMember("username", "password", "email@naver.com", "nickname", Authority.ROLE_USER);
    }

    public static Member createMember2() {
        return Member.createMember("username2", "password", "email2@naver.com", "nickname2", Authority.ROLE_USER);
    }
}
