package com.example.handoverbackend.service.message;

import static com.example.handoverbackend.factory.MemberMaker.createMember;
import static com.example.handoverbackend.factory.MemberMaker.createMember2;
import static com.example.handoverbackend.factory.MessageMaker.createMessage;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.message.Message;
import com.example.handoverbackend.dto.message.MessageCreateRequestDto;
import com.example.handoverbackend.dto.message.MessageFindAllWithPagingResponseDto;
import com.example.handoverbackend.dto.message.MessageResponseDto;
import com.example.handoverbackend.factory.MessageMaker;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.repository.message.MessageRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @InjectMocks
    MessageService messageService;

    @Mock
    MessageRepository messageRepository;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("쪽지 작성 테스트")
    void writeMessageTest() {
        //given
        Member sender = createMember();
        Member receiver = createMember2();
        MessageCreateRequestDto req = new MessageCreateRequestDto("제목","내용","username2");
        Message message = createMessage(req.getTitle(), req.getContent(), sender, receiver);
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(receiver));
        given(messageRepository.save(any())).willReturn(message);

        //when
        MessageResponseDto messageResponseDto = messageService.writeMessage(sender, req);

        //then
        assertThat(messageResponseDto.getContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("발신함 전체 조회 테스트")
    void findAllSentMessagesTest() {
        //given
        Member sender = createMember();
        Member receiver = createMember2();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        List<Message> messages = List.of(MessageMaker.createMessage("제목","내용", sender, receiver), MessageMaker.createMessage("제목","내용", sender, receiver));
        Page<Message> allMessages = new PageImpl<>(messages);
        given(messageRepository.findAllBySenderUsername(sender.getUsername(), pageable)).willReturn(allMessages);

        //when
        MessageFindAllWithPagingResponseDto result = messageService.findAllSentMessages(sender);

        //then
        assertThat(result.getMessages().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("수신함 전체 조회 테스트")
    void findAllReceivedMessagesTest() {
        //given
        Member sender = createMember();
        Member receiver = createMember2();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        List<Message> messages = List.of(MessageMaker.createMessage("제목","내용", sender, receiver), MessageMaker.createMessage("제목","내용", sender, receiver));
        Page<Message> allMessages = new PageImpl<>(messages);
        given(messageRepository.findAllByReceiverUsername(receiver.getUsername(), pageable)).willReturn(allMessages);

        //when
        MessageFindAllWithPagingResponseDto result = messageService.findAllReceivedMessages(receiver);

        //then
        assertThat(result.getMessages().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("발신함 단건 조회 테스트")
    void findSentMessageTest() {
        //given
        Long id = 1L;
        Member sender = createMember();
        Member receiver = createMember2();
        Message message = createMessage("제목","내용", sender, receiver);
        given(messageRepository.findByIdAndSenderUsername(id, sender.getUsername())).willReturn(Optional.of(message));

        //when
        MessageResponseDto result = messageService.findSentMessage(id, sender);

        //then
        assertThat(result.getContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("수신함 단건 조회 테스트")
    void findReceivedMessageTest() {
        //given
        Long id = 1L;
        Member sender = createMember();
        Member receiver = createMember2();
        Message message = createMessage("제목","내용", sender, receiver);

        given(messageRepository.findByIdAndReceiverUsername(id, receiver.getUsername())).willReturn(Optional.of(message));

        //when
        MessageResponseDto result = messageService.findReceivedMessage(id, receiver);

        //then
        assertThat(result.getContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("쪽지 삭제 테스트(수신함)")
    void deleteMessageByReceiverTest() {
        //given
        Long id = 1L;
        Member sender = createMember();
        Member receiver = createMember2();
        Message message = createMessage("제목","내용", sender, receiver);
        given(messageRepository.findByIdAndReceiverUsername(id, receiver.getUsername())).willReturn(Optional.of(message));

        //when
        String result = messageService.deleteMessageByReceiver(id, receiver);

        //then
        assertThat(result).isEqualTo("받은 쪽지를 삭제하였습니다.");
        assertThat(message.isDeletedByReceiver()).isEqualTo(true);
    }

    @Test
    @DisplayName("쪽지 삭제 테스트(발신함)")
    void deleteMessageBySenderTest() {
        //given
        Long id = 1L;
        Member sender = createMember();
        Member receiver = createMember2();
        Message message = createMessage("제목","내용", sender, receiver);
        given(messageRepository.findByIdAndSenderUsername(id, sender.getUsername())).willReturn(Optional.of(message));

        //when
        String result = messageService.deleteMessageBySender(id, sender);

        //then
        assertThat(result).isEqualTo("보낸 쪽지를 삭제하였습니다.");
        assertThat(message.isDeletedBySender()).isEqualTo(true);
    }

    @Test
    @DisplayName("쪽지 삭제 테스트(레포지토리에서 삭제)")
    void deleteMessageTest() {
        //given
        Long id = 1L;
        Member sender = createMember();
        Member receiver = createMember2();
        Message message = createMessage("제목","내용", sender, receiver);
        given(messageRepository.findByIdAndSenderUsername(id, sender.getUsername())).willReturn(Optional.of(message));
        given(messageRepository.findByIdAndReceiverUsername(id, receiver.getUsername())).willReturn(Optional.of(message));

        //when
        String result1 = messageService.deleteMessageBySender(id, sender);
        String result2 = messageService.deleteMessageByReceiver(id, receiver);

        //then
        assertThat(result1).isEqualTo("보낸 쪽지를 삭제하였습니다.");
        assertThat(result2).isEqualTo("받은 쪽지를 삭제하였습니다.");
        verify(messageRepository).delete(message);
    }
}
