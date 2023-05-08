package com.example.handoverbackend.repository.comment;

import com.example.handoverbackend.domain.comment.MatchComment;
import com.example.handoverbackend.domain.match.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchCommentRepository extends JpaRepository<MatchComment, Long> {

    Page<MatchComment> findAllByMatch(Match match, Pageable pageable);
}
