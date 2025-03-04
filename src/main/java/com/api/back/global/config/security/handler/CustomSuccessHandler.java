package com.api.back.global.config.security.handler;

import com.api.back.domain.member.repository.MemberRepository;
import com.api.back.domain.member.domain.Member;
import com.api.back.global.config.security.dto.CustomOAuth2User;
import com.api.back.global.config.security.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${redirect.url}")
    String redirectUrl;

    @Value("${redirect.onboarding.url}")
    String onboardingUrl;

    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    public CustomSuccessHandler(JWTUtil jwtUtil, MemberRepository memberRepository) {

        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        Long username = customUserDetails.getUserName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 토큰 생성

        log.info("customUserDetails -> {}", customUserDetails.getEmail());

        Optional<Member> member = memberRepository.findByEmail(customUserDetails.getEmail() == null ? "" : customUserDetails.getEmail()); //임의 값

        // TODO : 첫 로그인 분기처리
        member.ifPresentOrElse(existingMember -> {

            String refresh = jwtUtil.createJwt("refresh", member.get().getId(), role, 86400000L);

            log.info("현재 로그인 유저 -> {}", customUserDetails.getUserName());
            existingMember.updateEmail(customUserDetails.getEmail());
            existingMember.updateName(customUserDetails.getName());
            existingMember.updateRefreshToken(refresh);

            memberRepository.save(existingMember);

            response.addCookie(createCookie("Authorization", refresh));
// Set-Cookie 헤더에 SameSite=None 추가
            response.addHeader("Set-Cookie", "Authorization=" + refresh + "; Path=/; Secure; HttpOnly; SameSite=None");
            redirectUrl = redirectUrl + "?Authorization=" + refresh;
            log.info("new refreshToken -> {}", refresh);
            log.info("redirect url -> {}", redirectUrl +"?refresh=" + refresh);

            try {
                response.sendRedirect(redirectUrl);  // IOException 처리
            } catch (IOException e) {
                log.error("Redirect failed", e);
            }
        }, () -> {
            // 값이 없을 경우 처리 (첫 로그인)
            log.info("첫 로그인 유저 -> {}", customUserDetails.getUserName());
            Member newMember = memberRepository.save(Member.builder()
                .googleId(customUserDetails.getGoogleId())
                .name(customUserDetails.getName())
                .email(customUserDetails.getEmail())
                .profile(customUserDetails.getProfile())
                .role("ROLE_USER")
                .refreshToken(String.valueOf(UUID.randomUUID()))
                .build());


            Long userId = newMember.getId(); // 자동 생성된 ID 값
            String refresh = jwtUtil.createJwt("refresh", userId, role, 86400000L);

            newMember.updateRefreshToken(refresh);

            memberRepository.save(newMember);

            response.addCookie(createCookie("Authorization", refresh));

            // Set-Cookie 헤더에 SameSite=None 추가
            response.addHeader("Set-Cookie", "Authorization=" + refresh + "; Path=/; Secure; HttpOnly; SameSite=None");

            redirectUrl = redirectUrl + "?Authorization=" + refresh;
            log.info("new refreshToken -> {}", refresh);

            log.info("redirect url -> {}", redirectUrl +"?refresh=" + refresh);

            try {
                response.sendRedirect(redirectUrl);  // IOException 처리
            } catch (IOException e) {
                log.error("Redirect failed", e);
            }
        });

    }


    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        cookie.setSecure(true);   //https에 대해서만 허용할지 여부
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}