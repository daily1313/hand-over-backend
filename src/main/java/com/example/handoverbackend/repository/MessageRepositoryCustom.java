package com.example.handoverbackend.repository;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.message.Message;
import com.example.handoverbackend.dto.message.MessageCreateRequestDto;
import com.example.handoverbackend.dto.message.MessageResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageRepositoryCustom {
    Page<MessageResponseDto> findSentMessages(String senderUsername, Pageable pageable);
    Page<MessageResponseDto> findReceivedMessages(String receiverUsername, Pageable pageable);

    Optional<Message> findByIdWithReceiver(Long receivedMessageId, String receiverUsername);
    Optional<Message> findByIdWithSender(Long sentMessageId, String senderUsername);
}
