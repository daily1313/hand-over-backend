package com.example.handoverbackend.repository.match;

import com.example.handoverbackend.domain.match.Category;
import com.example.handoverbackend.domain.match.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {

    Page<Match> findAllByCategory(Category category, Pageable pageable);

    Page<Match> findAllByOrderByPriceDesc(Pageable pageable);

    Page<Match> findAllByOrderByPrice(Pageable pageable);

    Page<Match> findAllByMatchNameContaining(String keyword, Pageable pageable);

    Page<Match> findAllByAddressContaining(String keyword, Pageable pageable);
}
