package com.example.handoverbackend.controller.message;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.message.MessageCreateRequestDto;
import com.example.handoverbackend.exception.MemberNotFoundException;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.response.Response;
import com.example.handoverbackend.service.message.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Message Controller", tags = "Messages")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController {

    private static final String DEFAULT_PAGE = "0";

    private final MessageService messageService;
    private final MemberRepository memberRepository;

    @Operation(summary = "쪽지 보내기", description = "쪽지를 전송하였습니다.")
    @PostMapping("/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public Response writeMessage(@Valid @RequestBody MessageCreateRequestDto req) {
        Member sender = getPrincipal();
        return Response.success(messageService.writeMessage(sender, req));
    }

    @Operation(summary = "특정 회원의 전체 메세지함 조회", description = "전체 쪽지를 확인하였습니다.(수신함 + 발신함)")
    @GetMapping("/messages")
    public Response findAllMessages(@RequestParam(defaultValue = DEFAULT_PAGE) Integer page) {
        Member member = getPrincipal();
        return Response.success(messageService.findAllMessagesByMember(member, page));
    }

    @Operation(summary = "발신함 확인", description = "보낸 쪽지를 확인하였습니다.")
    @GetMapping("/messages/sender")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllSentMessages(@RequestParam(defaultValue = DEFAULT_PAGE) Integer page) {
        Member sender = getPrincipal();
        return Response.success(messageService.findAllSentMessages(sender, page));
    }

    @Operation(summary = "수신함 확인", description = "받은 쪽지를 확인하였습니다.")
    @GetMapping("/messages/receiver")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllReceivedMessages(@RequestParam(defaultValue = DEFAULT_PAGE) Integer page) {
        Member receiver = getPrincipal();
        return Response.success(messageService.findAllReceivedMessages(receiver, page));
    }

    @Operation(summary = "발신함 쪽지 1개 확인", description = "보낸 쪽지 1개를 확인하였습니다.")
    @GetMapping("/messages/sender/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response findSentMessage(@PathVariable Long id) {
        Member sender = getPrincipal();
        return Response.success(messageService.findSentMessage(id, sender));
    }

    @Operation(summary = "수신함 쪽지 1개 확인", description = "받은 쪽지 1개를 확인하였습니다.")
    @GetMapping("/messages/receiver/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response findReceivedMessage(@PathVariable Long id) {
        Member receiver = getPrincipal();
        return Response.success(messageService.findReceivedMessage(id, receiver));
    }

    @Operation(summary = "발신함 메세지 삭제", description = "발신함 메세지를 삭제하였습니다.")
    @DeleteMapping("/messages/sender/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteMessageBySender(@PathVariable Long id) {
        Member sender = getPrincipal();
        return Response.success(messageService.deleteMessageBySender(id, sender));
    }

    @Operation(summary = "수신함 메세지 삭제", description = "수신함 메세지를 삭제하였습니다.")
    @DeleteMapping("/messages/receiver/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteMessageByReceiver(@PathVariable Long id) {
        Member receiver = getPrincipal();
        return Response.success(messageService.deleteMessageByReceiver(id, receiver));
    }

    // 유저 정보를 가져오는 getPrincipal 함수
    private Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName())
                .orElseThrow(MemberNotFoundException::new);
        return member;
    }
}
