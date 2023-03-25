package com.example.handoverbackend.factory;

import com.example.handoverbackend.domain.board.Board;
import com.example.handoverbackend.domain.board.Image;
import com.example.handoverbackend.domain.category.Category;
import com.example.handoverbackend.domain.member.Member;

import java.util.List;

import static com.example.handoverbackend.factory.ImageMaker.createImage;
import static com.example.handoverbackend.factory.ImageMaker.createImages;
import static com.example.handoverbackend.factory.MemberMaker.*;

public class BoardMaker {

    public static Board createBoard(){
        return Board.createBoard("title", "content", createMember(), Category.createCategory("name"), List.of(createImage()));
    }
}
