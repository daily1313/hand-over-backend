package com.example.handoverbackend.controller.message;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.message.MessageCreateRequestDto;
import com.example.handoverbackend.exception.MemberNotFoundException;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.response.Response;
import com.example.handoverbackend.service.message.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Message Controller", tags = "Messages")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "쪽지 보내기", notes = "쪽지를 전송하였습니다.")
    @PostMapping("/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public Response writeMessage(@Valid @RequestBody MessageCreateRequestDto req) {
        Member sender = getPrincipal();
        return Response.success(messageService.writeMessage(sender, req));
    }

    @ApiOperation(value = "발신함 확인", notes = "보낸 쪽지를 확인하였습니다.")
    @GetMapping("/messages/sender")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllSentMessages() {
        Member sender = getPrincipal();
        return Response.success(messageService.findAllSentMessages(sender));
    }

    @ApiOperation(value = "수신함 확인", notes = "받은 쪽지를 확인하였습니다.")
    @GetMapping("/messages/receiver")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllReceivedMessages() {
        Member receiver = getPrincipal();
        return Response.success(messageService.findAllReceivedMessages(receiver));
    }

    @ApiOperation(value = "발신함 쪽지 1개 확인", notes = "보낸 쪽지 1개를 확인하였습니다.")
    @GetMapping("/messages/sender/{sentMessageId}")
    @ResponseStatus(HttpStatus.OK)
    public Response findSentMessage(@PathVariable Long sentMessageId) {
        Member sender = getPrincipal();
        return Response.success(messageService.findSentMessage(sentMessageId, sender));
    }

    @ApiOperation(value = "수신함 쪽지 1개 확인", notes = "받은 쪽지 1개를 확인하였습니다.")
    @GetMapping("/messages/receiver/{receivedMessageId}")
    @ResponseStatus(HttpStatus.OK)
    public Response findReceivedMessage(@PathVariable Long receivedMessageId) {
        Member receiver = getPrincipal();
        return Response.success(messageService.findReceivedMessage(receivedMessageId, receiver));
    }

    @ApiOperation(value = "발신함 메세지 삭제", notes = "발신함 메세지를 삭제하였습니다.")
    @DeleteMapping("/messages/sender/{sentMessageId}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteMessageBySender(@PathVariable Long sentMessageId) {
        Member sender = getPrincipal();
        return Response.success(messageService.deleteMessageBySender(sentMessageId, sender));
    }

    @ApiOperation(value = "수신함 메세지 삭제", notes = "수신함 메세지를 삭제하였습니다.")
    @DeleteMapping("/messages/receiver/{receivedMessageId}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteMessageByReceiver(@PathVariable Long receivedMessageId) {
        Member receiver = getPrincipal();
        return Response.success(messageService.deleteMessageByReceiver(receivedMessageId, receiver));
    }

    // 유저 정보를 가져오는 getPrincipal 함수
    private Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName())
                .orElseThrow(MemberNotFoundException::new);
        return member;
    }
}
