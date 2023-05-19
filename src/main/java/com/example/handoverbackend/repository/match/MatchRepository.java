package com.example.handoverbackend.repository.match;

import com.example.handoverbackend.domain.match.Category;
import com.example.handoverbackend.domain.match.Match;
import com.example.handoverbackend.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {

    Page<Match> findAllBySeller(Member seller, Pageable pageable);

    Page<Match> findAllByCategory(Category category, Pageable pageable);

    Page<Match> findAllByOrderByPriceDesc(Pageable pageable);

    Page<Match> findAllByOrderByPrice(Pageable pageable);

    Page<Match> findAllByMatchNameContainingOrAddressContaining(String matchName, String address, Pageable pageable);
}
