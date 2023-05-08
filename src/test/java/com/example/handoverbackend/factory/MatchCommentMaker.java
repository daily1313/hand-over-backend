package com.example.handoverbackend.factory;

import com.example.handoverbackend.domain.comment.MatchComment;
import com.example.handoverbackend.domain.match.Match;
import com.example.handoverbackend.domain.member.Member;

public class MatchCommentMaker {

    public static MatchComment createComment(Match match, Member member) {
        return new MatchComment(member, match, "content");
    }
}
