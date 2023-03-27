package com.example.handoverbackend.service.message;


import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.message.Message;
import com.example.handoverbackend.dto.message.MessageCreateRequestDto;
import com.example.handoverbackend.dto.message.MessageResponseDto;
import com.example.handoverbackend.exception.MemberNotEqualsException;
import com.example.handoverbackend.exception.MemberNotFoundException;
import com.example.handoverbackend.exception.MessageNotFoundException;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MessageService {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE = 0;
    private static final String DELETE_SUCCESS_RECEIVED_MESSAGE = "받은 쪽지를 삭제하였습니다.";
    private static final String DELETE_SUCCESS_SENT_MESSAGE = "보낸 쪽지를 삭제하였습니다.";

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public MessageResponseDto writeMessage(Member sender, MessageCreateRequestDto req) {
        Member receiver = memberRepository.findByUsername(req.getReceiverUsername())
                .orElseThrow(MemberNotFoundException::new);
        Message message = Message.createMessage(req.getTitle(), req.getContent(), sender, receiver);
        messageRepository.save(message);
        return MessageResponseDto.toDto(message);
    }

    @Transactional(readOnly = true)
    public Page<MessageResponseDto> findAllSentMessages(Member sender) {
        Pageable pageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_PAGE_SIZE, Sort.by("createdAt").descending());
        Page<MessageResponseDto> findAllMessages = messageRepository.findSentMessages(sender.getUsername(), pageable);
        return findAllMessages;
    }

    @Transactional(readOnly = true)
    public Page<MessageResponseDto> findAllReceivedMessages(Member receiver) {
        Pageable pageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_PAGE_SIZE, Sort.by("createdAt").descending());
        Page<MessageResponseDto> findAllMessages = messageRepository.findReceivedMessages(receiver.getUsername(), pageable);
        return findAllMessages;
    }

    @Transactional(readOnly = true)
    public MessageResponseDto findSentMessage(Long sentMessageId, Member sender) {
        Message message = messageRepository.findByIdWithSender(sentMessageId, sender.getUsername()).orElseThrow(MessageNotFoundException::new);
        return MessageResponseDto.toDto(message);
    }

    @Transactional(readOnly = true)
    public MessageResponseDto findReceivedMessage(Long receivedMessageId, Member receiver) {
        Message message = messageRepository.findByIdWithReceiver(receivedMessageId, receiver.getUsername()).orElseThrow(MessageNotFoundException::new);
        return MessageResponseDto.toDto(message);
    }

    @Transactional
    public String deleteMessageByReceiver(Long id, Member receiver) {

        Message message = messageRepository.findByIdWithReceiver(id, receiver.getUsername()).orElseThrow(
                MessageNotFoundException::new);

        if(!message.getReceiver().equals(receiver)) {
            throw new MemberNotEqualsException();
        }

        message.deletedByReceiver();

        if(isDeletedBySenderAndReceiver(id)) {
            messageRepository.deleteById(id);
            return DELETE_SUCCESS_RECEIVED_MESSAGE;
        }

        return DELETE_SUCCESS_RECEIVED_MESSAGE;
    }

    @Transactional
    public String deleteMessageBySender(Long id, Member sender) {

        Message message = messageRepository.findByIdWithSender(id, sender.getUsername()).orElseThrow(
                MessageNotFoundException::new);

        if(!message.getSender().equals(sender)) {
            throw new MemberNotEqualsException();
        }

        message.deletedBySender();

        if(isDeletedBySenderAndReceiver(id)) {
            messageRepository.deleteById(id);
            return DELETE_SUCCESS_SENT_MESSAGE;
        }
        return DELETE_SUCCESS_SENT_MESSAGE;
    }

    private boolean isDeletedBySenderAndReceiver(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        if(message.isDeletable()) {
            return true;
        }
        return false;
    }
}
