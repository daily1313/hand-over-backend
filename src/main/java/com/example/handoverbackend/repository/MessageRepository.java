package com.example.handoverbackend.repository;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.message.Message;
import com.example.handoverbackend.dto.message.MessageResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findBySender(Member sender, Pageable pageable);

    Page<Message> findByReceiver(Member receiver, Pageable pageable);

    Page<MessageResponseDto> findAllBySenderUsername(String senderUsername, Pageable pageable);

    Page<MessageResponseDto> findAllByReceiverUsername(String receiverUsername, Pageable pageable);

    Optional<Message> findByIdAndReceiverUsername(Long receivedMessageId, String receiverUsername);

    Optional<Message> findByIdAndSenderUsername(Long sentMessageId, String senderUsername);
}
