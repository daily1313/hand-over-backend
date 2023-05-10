package com.example.handoverbackend.controller.message;

import static com.example.handoverbackend.factory.MemberMaker.createMember;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.message.MessageCreateRequestDto;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.service.message.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @InjectMocks
    MessageController messageController;

    @Mock
    MessageService messageService;

    @Mock
    MemberRepository memberRepository;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
    }

    @Test
    @DisplayName("쪽지 작성 테스트")
    void writeMessageTest() throws Exception {
        //given
        Member member = createMember();
        MessageCreateRequestDto req = new MessageCreateRequestDto("title", "content", "username");
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
        ).andExpect(status().isCreated());

        //then
        verify(messageService).writeMessage(refEq(member), refEq(req));
    }

    @Test
    @DisplayName("수신함 메세지 전체 조회 테스트")
    void findAllReceivedMessagesTest() throws Exception {
        //given
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                get("/api/messages/receiver")
        ).andExpect(status().isOk());

        //then
        verify(messageService).findAllReceivedMessages(member);
    }

    @Test
    @DisplayName("발신함 메세지 전체 조회 테스트")
    void findAllSentMessagesTest() throws Exception {
        //given
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                get("/api/messages/sender")
        ).andExpect(status().isOk());

        //then
        verify(messageService).findAllSentMessages(member);
    }

    @Test
    @DisplayName("수신함 메세지 단건 조회 테스트")
    void findReceivedMessageTest() throws Exception {
        //given
        Long receivedMessageId = 1L;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                get("/api/messages/receiver/{receivedMessageId}",receivedMessageId)
        ).andExpect(status().isOk());

        //then
        verify(messageService).findReceivedMessage(receivedMessageId, member);
    }

    @Test
    @DisplayName("발신함 메세지 단건 조회 테스트")
    void findSentMessageTest() throws Exception {
        //given
        Long sentMessageId = 1L;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                get("/api/messages/sender/{sentMessageId}",sentMessageId)
        ).andExpect(status().isOk());

        //then
        verify(messageService).findSentMessage(sentMessageId, member);
    }

    @Test
    @DisplayName("발신함 메세지 삭제 테스트")
    void deleteMessageBySenderTest() throws Exception {
        //given
        Long sentMessageId = 1L;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                delete("/api/messages/sender/{sentMessageId}",sentMessageId)
        ).andExpect(status().isOk());

        //then
        verify(messageService).deleteMessageBySender(sentMessageId, member);
    }

    @Test
    @DisplayName("수신함 메세지 삭제 테스트")
    void deleteMessageByReceiverTest() throws Exception {
        //given
        Long receivedMessageId = 1L;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                delete("/api/messages/sender/{receivedMessageId}", receivedMessageId)
        ).andExpect(status().isOk());

        //then
        verify(messageService).deleteMessageBySender(receivedMessageId, member);
    }
}
