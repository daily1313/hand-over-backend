package com.example.handoverbackend.factory;

import com.example.handoverbackend.domain.member.Authority;
import com.example.handoverbackend.domain.member.Member;

public class MemberMaker {

    public static Member createMember(){
        return Member.createMember("username", "password", "name", "email@naver.com", "nickname", Authority.ROLE_USER);
    }
}
