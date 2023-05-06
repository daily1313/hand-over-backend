package com.example.handoverbackend.domain.report;

import com.example.handoverbackend.domain.common.BaseEntity;
import com.example.handoverbackend.domain.match.Match;
import com.example.handoverbackend.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "reporter_id", nullable = false)
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "reported_id", nullable = false)
    private Match reported;

    @Column(nullable = false)
    private String content;

    public MatchReport(Member reporter, Match reported, String content) {
        this.reporter = reporter;
        this.reported = reported;
        this.content = content;
    }
}

