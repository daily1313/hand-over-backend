package com.example.handoverbackend.controller.auth;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.handoverbackend.dto.jwt.LoginRequestDto;
import com.example.handoverbackend.dto.jwt.SignUpRequestDto;
import com.example.handoverbackend.dto.jwt.TokenResponseDto;
import com.example.handoverbackend.service.auth.AuthService;
import com.example.handoverbackend.service.email.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    AuthController authController;

    @Mock
    AuthService authService;

    @Mock
    EmailService emailService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void joinTest() throws Exception {
        // given
        SignUpRequestDto req = new SignUpRequestDto("user", "user31123!", "김승범", "kimsb7218@naver.com", "안녕안녕", "dddda123");

        // when
        mockMvc.perform(
                        post("/api/auth/join")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
        // then
        verify(authService).join(req);
    }
    
    @Test
    @DisplayName("로그인 테스트")
    void loginTest() throws Exception {
        //given
        LoginRequestDto req = new LoginRequestDto("username","daily13133!!");
        given(authService.login(req)).willReturn(new TokenResponseDto("access","refresh"));

        //when
        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.data.accessToken").value("access"))
                .andExpect(jsonPath("$.result.data.refreshToken").value("refresh"));
        //then
        verify(authService).login(req);
    }
    
    @Test
    @DisplayName("이메일 인증코드 전송 테스트")
    void mailConfirmTest() throws Exception {
        //given
        String email = "kimsb7218@naver.com";

        //when
        mockMvc.perform(
                post("/api/auth/join/email/mailConfirm")
                        .param("email", email)
        ).andExpect(status().isOk());

        //then
        verify(emailService).sendSimpleMessage(email);
    }

    @Test
    @DisplayName("이메일 인증번호 확인 테스트")
    void confirmEmailCertificationCodeTest() throws Exception {
        //given
        String code = "dasda";

        //when
        mockMvc.perform(
                post("/api/auth/join/email/check")
                        .param("code", code)
        ).andExpect(status().isOk());

        //then
        verify(authService).confirmEmailCertificationCode(code);
    }
}
