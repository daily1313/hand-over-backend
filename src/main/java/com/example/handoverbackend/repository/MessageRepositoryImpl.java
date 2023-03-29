package com.example.handoverbackend.repository;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.member.QMember;
import com.example.handoverbackend.domain.message.Message;
import com.example.handoverbackend.dto.message.MessageResponseDto;
import com.example.handoverbackend.dto.message.QMessageResponseDto;
import com.example.handoverbackend.exception.MemberNotFoundException;
import com.example.handoverbackend.exception.MessageNotFoundException;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Path;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.example.handoverbackend.domain.message.QMessage.message;
import static com.example.handoverbackend.domain.member.QMember.member;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final MemberRepository memberRepository;

    @Override
    public Page<MessageResponseDto> findSentMessages(String senderUsername, Pageable pageable) {

        QMember sender = new QMember("sender");
        QMember receiver = new QMember("receiver");

        List<MessageResponseDto> content = queryFactory.select(
                        new QMessageResponseDto(
                                message.title, message.content,
                                sender.username.as("senderUsername"),
                                receiver.username.as("receiverUsername")))
                .from(message)
                .join(message.sender, sender)
                .leftJoin(message.receiver, receiver)
                .where(sender.username.eq(senderUsername))
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(message)
                .join(message.sender, sender)
                .where(sender.username.eq(senderUsername))
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MessageResponseDto> findReceivedMessages(String receiverUsername, Pageable pageable) {

        QMember sender = new QMember("sender");
        QMember receiver = new QMember("receiver");

        List<MessageResponseDto> content = queryFactory.select(
                        new QMessageResponseDto(
                                message.title, message.content,
                                sender.username.as("senderUsername"),
                                receiver.username.as("receiverUsername")))
                .from(message)
                .join(message.receiver, receiver)
                .leftJoin(message.sender, sender)
                .where(receiver.username.eq(receiverUsername))
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(message)
                .join(message.receiver, receiver)
                .where(receiver.username.eq(receiverUsername))
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<Message> findByIdWithReceiver(Long receivedMessageId, String receiverUsername) {
        Message result = queryFactory.selectFrom(message)
                .join(message.receiver, member)
                .where(message.deletedByReceiver.eq(false), message.id.eq(receivedMessageId), member.username.eq(receiverUsername))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Message> findByIdWithSender(Long sentMessageId, String senderUsername) {
        Message result = queryFactory.selectFrom(message)
                .join(message.sender, member)
                .where(message.deletedBySender.eq(false), message.id.eq(sentMessageId), member.username.eq(senderUsername))
                .fetchOne();
        return Optional.ofNullable(result);
    }
}
