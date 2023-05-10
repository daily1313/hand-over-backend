package com.example.handoverbackend.service.auth;


import static com.example.handoverbackend.factory.MemberMaker.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


import com.example.handoverbackend.domain.member.EmailAuth;
import com.example.handoverbackend.dto.jwt.LoginRequestDto;
import com.example.handoverbackend.dto.jwt.SignUpRequestDto;
import com.example.handoverbackend.dto.jwt.TokenResponseDto;
import com.example.handoverbackend.exception.EmailAlreadyExistException;
import com.example.handoverbackend.exception.LoginFailureException;
import com.example.handoverbackend.exception.NicknameAlreadyExistException;
import com.example.handoverbackend.exception.UsernameAlreadyExistException;
import com.example.handoverbackend.repository.EmailAuthRepository;
import com.example.handoverbackend.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    EmailAuthRepository emailAuthRepository;


    @Test
    @DisplayName("회원가입 테스트")
    void joinTest() {
        //given
        String ePw = "j8GI7RHe";
        SignUpRequestDto req = new SignUpRequestDto("kimsb7218", "dsadsasda111!", "김승범", "kimsb7218@naver.com", "김밥천국", ePw);
        EmailAuth emailAuth = new EmailAuth(ePw, "kimsb7218");
        given(emailAuthRepository.findEmailAuthByEmail(req.getEmail())).willReturn(Optional.of(emailAuth));

        //when
        authService.join(req);

        //then
        verify(passwordEncoder).encode(req.getPassword());
        verify(memberRepository).save(any());
    }

    @Test
    @DisplayName("이메일 인증 코드 확인 테스트")
    void confirmEmailCertificationCodeTest() {
        //given
        String code = "j8GI7RHe";
        EmailAuth emailAuth = new EmailAuth(code, "kimsb7218@naver.com");
        given(emailAuthRepository.existsByKey(code)).willReturn(true);

        //when
        String result = authService.confirmEmailCertificationCode(code);

        //then
        assertThat(result).isEqualTo("인증 번호가 확인되었습니다.");
    }

    @Test
    @DisplayName("이메일 중복 테스트")
    void emailAlreadyExistExceptionTest() {
        //given
        SignUpRequestDto req = new SignUpRequestDto("kimsb7218", "dsadsasda111!", "김승범", "kimsb7218@naver.com", "김밥천국", "12345678");
        given(memberRepository.existsByEmail(anyString())).willReturn(true);

        //when, then
        assertThatThrownBy(() -> authService.join(req))
                .isInstanceOf(EmailAlreadyExistException.class);
    }

    @Test
    @DisplayName("닉네임 중복 테스트")
    public void nicknameAlreadyExistExceptionTest() {
        //given
        SignUpRequestDto req = new SignUpRequestDto("kimsb7218", "dsadsasda111!", "김승범", "kimsb7218@naver.com", "김밥천국", "12345678");
        given(memberRepository.existsByNickname(anyString())).willReturn(true);

        //when, then
        assertThatThrownBy(() -> authService.join(req))
                .isInstanceOf(NicknameAlreadyExistException.class);
    }

    @Test
    @DisplayName("아이디 중복 테스트")
    void usernameAlreadyExistExceptionTest() {
        //given
        SignUpRequestDto req = new SignUpRequestDto("kimsb7218", "dsadsasda111!", "김승범", "kimsb7218@naver.com", "김밥천국", "12345678");
        given(memberRepository.existsByUsername(anyString())).willReturn(true);

        //when, then
        assertThatThrownBy(() -> authService.join(req))
                .isInstanceOf(UsernameAlreadyExistException.class);
    }

    @Test
    @DisplayName("로그인 실패 테스트(아이디 잘못 입력시)")
    void loginFailureExceptionByUsernameTest() {
        // given
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(createMember()));

        //when, then
        assertThatThrownBy(() -> authService.login(new LoginRequestDto("username1","password")))
                .isInstanceOf(LoginFailureException.class);
    }

    @Test
    @DisplayName("로그인 실패 테스트(비밀번호 잘못 입력시)")
    void loginFailureExceptionByPasswordTest() {
        // given
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(createMember()));

        //when, then
        assertThatThrownBy(() -> authService.login(new LoginRequestDto("username","password1")))
                .isInstanceOf(LoginFailureException.class);
    }
}
