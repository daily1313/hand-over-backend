package com.example.handoverbackend.domain.board;

import com.example.handoverbackend.domain.common.BaseEntity;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.ticket.Ticket;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    public static Favorite createFavorite(Board board, Member member) {
        return new Favorite(board, member);
    }

    private Favorite(Board board, Member member) {
        this.board = board;
        this.member = member;
    }

    public Favorite(Ticket ticket, Member member) {
        this.ticket = ticket;
        this.member = member;
    }
}
