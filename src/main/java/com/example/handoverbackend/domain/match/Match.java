package com.example.handoverbackend.domain.match;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.match.MatchEditRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "match_table")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member seller;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String matchName;

    @Column(nullable = false)
    private String address;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    @Lob
    private String detailsContent;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    @Lob
    private String precaution;

    @Column(nullable = false)
    private boolean isMatched;

    @Builder
    public Match(Member seller, Category category, String matchName, String address, LocalDateTime startDate, LocalDateTime endDate,
                 String detailsContent, int price, String precaution, boolean isMatched) {
        this.seller = seller;
        this.category = category;
        this.matchName = matchName;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
        this.detailsContent = detailsContent;
        this.price = price;
        this.precaution = precaution;
        this.isMatched = false;
    }

    public boolean isSeller(Member findMember) {
        return this.seller.equals(findMember);
    }

    public void changeStatus() {
        if(!isMatched) {
            isMatched = true;
            return;
        }
        isMatched = false;
    }

    public void editMatchInfo(MatchEditRequestDto req) {
        category = req.getCategory();
        matchName = req.getMatchName();
        address = req.getAddress();
        startDate = req.getStartDate();
        endDate = req.getEndDate();
        detailsContent = req.getDetailsContent();
        price = req.getPrice();
        precaution = req.getPrecaution();
    }

    public boolean isMatched() {
        return isMatched;
    }
}
