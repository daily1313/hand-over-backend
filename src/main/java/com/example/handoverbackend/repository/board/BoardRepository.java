package com.example.handoverbackend.repository.board;

import com.example.handoverbackend.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByCategoryId(Long categoryId, Pageable pageable);

    Page<Board> findAllByTitleContaining(String keyword, Pageable pageable);
}
