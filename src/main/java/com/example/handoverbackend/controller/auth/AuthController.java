package com.example.handoverbackend.controller.auth;

import static com.example.handoverbackend.response.Response.success;

import com.example.handoverbackend.dto.jwt.LoginRequestDto;
import com.example.handoverbackend.dto.jwt.SignUpRequestDto;
import com.example.handoverbackend.dto.jwt.TokenRequestDto;
import com.example.handoverbackend.response.Response;
import com.example.handoverbackend.service.auth.AuthService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final String JOIN_SUCCESS_MESSAGE = "회원 가입에 성공하였습니다.";

    @ApiOperation(value = "회원가입", notes = "회원가입 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/join")
    public Response register(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.join(signUpRequestDto);
        return success(JOIN_SUCCESS_MESSAGE);
    }

    @ApiOperation(value = "로그인", notes = "회원가입 후 로그인")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Response login(@Valid @RequestBody LoginRequestDto req) {
        return success(authService.login(req));
    }

    @ApiOperation(value = "토큰 재발급", notes = "토큰 재발급 요청")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reissue")
    public Response reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return success(authService.reissue(tokenRequestDto));
    }
}
