package com.example.handoverbackend.repository.match;

import com.example.handoverbackend.domain.favorite.MatchFavorite;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.match.Match;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchFavoriteRepository extends JpaRepository<MatchFavorite, Long> {

    Page<MatchFavorite> findAllByMember(Member member, Pageable pageable);

    Optional<MatchFavorite> findByMatchAndMember(Match match, Member member);

    boolean existsByMatchAndMember(Match match, Member member);
}
