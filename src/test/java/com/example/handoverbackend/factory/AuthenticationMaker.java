package com.example.handoverbackend.factory;

import com.example.handoverbackend.domain.member.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;

public class AuthenticationMaker {

    public static Authentication createAuthentication(Member member) {
        return new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
    }
}
