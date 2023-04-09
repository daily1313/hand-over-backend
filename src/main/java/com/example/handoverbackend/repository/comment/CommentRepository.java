package com.example.handoverbackend.repository.comment;

import com.example.handoverbackend.domain.board.Board;
import com.example.handoverbackend.domain.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByBoard(Board board, Pageable pageable);
}
