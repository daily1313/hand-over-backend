package com.example.handoverbackend.factory;

import com.example.handoverbackend.domain.board.Board;
import com.example.handoverbackend.domain.comment.Comment;
import com.example.handoverbackend.domain.member.Member;

public class CommentMaker {

    public static Comment createComment(Board board, Member member) {
        return new Comment(board, member, "content");
    }
}
