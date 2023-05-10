package com.example.handoverbackend.factory;

import com.example.handoverbackend.domain.board.Board;
import com.example.handoverbackend.domain.category.Category;
import com.example.handoverbackend.domain.member.Member;

import java.util.List;

import static com.example.handoverbackend.factory.ImageMaker.createImage;
import static com.example.handoverbackend.factory.MemberMaker.createMember;

public class BoardMaker {

    public static Board createBoard() {
        return Board.createBoard("title", "content", createMember(), new Category("name"), List.of(createImage()));
    }

    public static Board createBoard(Member member) {
        return Board.createBoard("title", "content", member, new Category("name"), List.of(createImage()));
    }
}
