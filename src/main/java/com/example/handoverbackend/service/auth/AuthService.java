package com.example.handoverbackend.service.auth;

import com.example.handoverbackend.config.jwt.TokenProvider;
import com.example.handoverbackend.domain.member.EmailAuth;
import com.example.handoverbackend.domain.member.Authority;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.member.RefreshToken;
import com.example.handoverbackend.dto.jwt.LoginRequestDto;
import com.example.handoverbackend.dto.jwt.SignUpRequestDto;
import com.example.handoverbackend.dto.jwt.TokenDto;
import com.example.handoverbackend.dto.jwt.TokenRequestDto;
import com.example.handoverbackend.dto.jwt.TokenResponseDto;
import com.example.handoverbackend.exception.EmailAlreadyExistException;
import com.example.handoverbackend.exception.EmailAuthNotEqualsException;
import com.example.handoverbackend.exception.EmailAuthNotFoundException;
import com.example.handoverbackend.exception.LoginFailureException;
import com.example.handoverbackend.exception.NicknameAlreadyExistException;
import com.example.handoverbackend.exception.UsernameAlreadyExistException;
import com.example.handoverbackend.repository.EmailAuthRepository;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailAuthRepository emailAuthRepository;

    /**
     * 회원가입 순서
     * 1. email 기반 인증번호 입력
     * 2. email + 인증번호를 EmailAuth 테이블에 저장
     * 3. signUp 메서드에서 회원가입시 EmailAuth 가져와서 비교
     */
    @Transactional
    public void join(SignUpRequestDto req) {
        validateSignUpInfo(req);
        Member member = createSignupFormOfUser(req);
        EmailAuth emailAuth = emailAuthRepository.findEmailAuthByEmail(req.getEmail()).orElseThrow(
                EmailAuthNotFoundException::new);
        if (emailAuth.getKey().equals(req.getEmailAuthKey())) {
            memberRepository.save(member);
            emailAuthRepository.delete(emailAuth);
        } else {
            throw new EmailAuthNotEqualsException(); // 이메일 인증 정보가 일치하지 않음
        }
    }

    @Transactional(readOnly = true)
    public String confirmEmailCertificationCode(String code) {
        if(emailAuthRepository.existsByKey(code)) {
            return "인증번호가 확인되었습니다.";
        } else {
            throw new EmailAuthNotEqualsException();
        }
    }


    @Transactional
    public TokenResponseDto login(LoginRequestDto req) {
        Member member = memberRepository.findByUsername(req.getUsername())
                .orElseThrow(LoginFailureException::new);

        validatePassword(req, member);
        Authentication authentication = getUserAuthentication(req);
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken refreshToken = buildRefreshToken(authentication, tokenDto);
        refreshTokenRepository.save(refreshToken);
        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }

    private RefreshToken buildRefreshToken(Authentication authentication, TokenDto tokenDto) {
        return RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
    }

    private Authentication getUserAuthentication(LoginRequestDto req) {
        UsernamePasswordAuthenticationToken authenticationToken = req.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return authentication;
    }


    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto) {
        validateRefreshToken(tokenRequestDto);

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
        validateRefreshTokenOwner(refreshToken, tokenRequestDto);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        TokenResponseDto tokenResponseDto = new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
        return tokenResponseDto;
    }

    private Member createSignupFormOfUser(SignUpRequestDto req) {
        Member member = Member.createMember(req.getUsername(), passwordEncoder.encode(req.getPassword()),
                req.getName(), req.getEmail(), req.getNickname(), Authority.ROLE_USER);

        return member;
    }


    private void validateSignUpInfo(SignUpRequestDto signUpRequestDto) {

        if (memberRepository.existsByUsername(signUpRequestDto.getUsername())) {
            throw new UsernameAlreadyExistException(signUpRequestDto.getUsername());
        }

        if (memberRepository.existsByNickname(signUpRequestDto.getNickname())) {
            throw new NicknameAlreadyExistException(signUpRequestDto.getNickname());
        }

        if(memberRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new EmailAlreadyExistException(signUpRequestDto.getEmail());
        }
    }

    private void validatePassword(LoginRequestDto loginRequestDto, Member member) {
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new LoginFailureException();
        }
    }

    private void validateRefreshToken(TokenRequestDto tokenRequestDto) {
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }
    }

    private void validateRefreshTokenOwner(RefreshToken refreshToken, TokenRequestDto tokenRequestDto) {
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }
    }
}
