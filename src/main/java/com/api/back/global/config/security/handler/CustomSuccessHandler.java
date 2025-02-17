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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    public CustomSuccessHandler(JWTUtil jwtUtil, MemberRepository memberRepository) {

        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUserName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        Member member = memberRepository.findByGoogleId(customUserDetails.getAttribute("sub"));

        if(member != null) {    //RefreshToken 저장
            member.updateRefreshToken(refresh);
            member.updateDate();
            memberRepository.save(member);
        }

        log.info("new refreshToken -> {}", refresh);

        response.addCookie(createCookie("Authorization", refresh));
        response.sendRedirect("http://hair-fe-smoky.vercel.app");
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);   //https에 대해서만 허용할지 여부
//        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}