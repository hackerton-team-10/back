package com.api.back.global.config.security;

import com.api.back.domain.member.repository.MemberRepository;
import com.api.back.domain.member.domain.Member;
import com.api.back.global.config.security.dto.CustomOAuth2User;
import com.api.back.global.config.security.dto.GoogleResponse;
import com.api.back.global.config.security.dto.MemberDto;
import com.api.back.global.config.security.dto.OAuth2Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("loadUser Method Call");
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String googleId = oAuth2Response.getProviderId();

        Member member = memberRepository.findByGoogleId(googleId);

        if(member == null) {

            memberRepository.save(Member.builder()
                    .googleId(googleId)
                    .name(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .profile(oAuth2Response.getProfile())
                    .role("ROLE_USER")
                    .build());

            return new CustomOAuth2User(MemberDto.builder()
                .username(googleId)
                .name(oAuth2Response.getName())
                .role("ROLE_USER")
                .build());
        }
        else {

            if (!member.getEmail().equals(oAuth2Response.getEmail()) ||
                !member.getName().equals(oAuth2Response.getName())) {

                // email 또는 name이 변경된 경우에만 update
                member.updateEmail(oAuth2Response.getEmail());
                member.updateName(oAuth2Response.getName());
                member.updateDate();

                memberRepository.save(member);
            }

            return new CustomOAuth2User(MemberDto.builder()
                .username(googleId)
                .name(oAuth2Response.getName())
                .role("ROLE_USER")
                .build());

        }

    }
}
