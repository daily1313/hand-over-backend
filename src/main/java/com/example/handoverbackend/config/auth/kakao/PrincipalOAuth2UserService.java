package com.example.handoverbackend.config.auth.kakao;

import com.example.handoverbackend.domain.member.Authority;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.exception.UserNotFoundException;
import com.example.handoverbackend.repository.MemberRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final PasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = initializeUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = createInfoByProvider(oAuth2User, userRequest);
        Member memberEntity = memberRepository.findByUsername(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                .orElseThrow(UserNotFoundException::new);
        if (isNotExistingMember(memberEntity)){
            memberEntity = createMember(oAuth2UserInfo);
            memberRepository.save(memberEntity);
        }
        return new PrincipalDetails(memberEntity, oAuth2User.getAttributes());
    }

    private boolean isNotExistingMember(Member member){
        return member == null;
    }

    private Member createMember(OAuth2UserInfo oAuth2UserInfo){
        return Member.builder()
                .username(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                .password(bCryptPasswordEncoder.encode("겟인데어"))
                .email(oAuth2UserInfo.getEmail())
                .authority(Authority.ROLE_USER)
                .providerId(oAuth2UserInfo.getProviderId())
                .provider(oAuth2UserInfo.getProvider())
                .build();
    }

    private OAuth2User initializeUser(OAuth2UserRequest userRequest){
        System.out.println("UserRequest : " + userRequest.getClientRegistration());
        System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("getAttributes : " + oAuth2User.getAttributes());
        return oAuth2User;
    }

    private OAuth2UserInfo createInfoByProvider(OAuth2User oAuth2User, OAuth2UserRequest userRequest){
        if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")){
            return new KakaoUserInfo((Map)oAuth2User.getAttributes().get("profile"));
        }
        return null;
    }
}
