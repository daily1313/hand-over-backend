package com.example.handoverbackend.controller.auth;

import static com.example.handoverbackend.response.Response.success;

import com.example.handoverbackend.dto.jwt.LoginRequestDto;
import com.example.handoverbackend.dto.jwt.SignUpRequestDto;
import com.example.handoverbackend.dto.jwt.TokenRequestDto;
import com.example.handoverbackend.response.Response;
import com.example.handoverbackend.service.auth.AuthService;
import com.example.handoverbackend.service.email.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    @Operation(summary = "회원가입", description = "회원가입 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/join")
    public Response register(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        return Response.success(authService.join(signUpRequestDto));
    }

    @Operation(summary = "로그인", description = "회원가입 후 로그인")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Response login(@Valid @RequestBody LoginRequestDto req) {
        return success(authService.login(req));
    }

    @Operation(summary = "유저 회원가입시 이메일 인증번호 전송", description = "유저 회원가입시 이메일 인증번호 전송")
    @PostMapping("/join/email/mailConfirm")
    @ResponseStatus(HttpStatus.OK)
    public Response mailConfirm(@RequestParam("email") String email) throws Exception {
        return Response.success(emailService.sendSimpleMessage(email));
    }

    @Operation(summary = "유저 회원가입시 이메일 인증 코드확인", description = "유저 회원가입시 이메일 인증 코드확인")
    @PostMapping("/join/email/check")
    @ResponseStatus(HttpStatus.OK)
    public Response confirmEmailCertificationCode(@RequestParam("key") String key) {
        return Response.success(authService.confirmEmailCertificationCode(key));
    }

    @Operation(summary = "토큰 재발급", description = "토큰 재발급 요청")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reissue")
    public Response reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return success(authService.reissue(tokenRequestDto));
    }
}
