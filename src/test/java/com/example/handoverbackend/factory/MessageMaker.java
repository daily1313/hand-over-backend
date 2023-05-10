package com.example.handoverbackend.factory;

import static com.example.handoverbackend.factory.MemberMaker.createMember;
import static com.example.handoverbackend.factory.MemberMaker.createMember2;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.message.Message;

public class MessageMaker {

    public static Message createMessage(String title, String content, Member sender, Member receiver) {
        return Message.createMessage(title, content, sender, receiver);
    }

    public static Message createMessage2() {
        return Message.createMessage("제목","내용", createMember() , createMember2());
    }
}
