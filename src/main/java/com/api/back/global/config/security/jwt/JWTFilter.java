package com.api.back.global.config.security.jwt;

import com.api.back.global.config.security.dto.CustomOAuth2User;
import com.api.back.global.config.security.dto.MemberDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");

        log.info("Authorization Headaer -> {}", authorization);

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            log.info("token is null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //Bearer 부분 제거 후 순수 토큰만 획득
        String accessToken = authorization.split(" ")[1];

        //토큰 만료 시간 검증
        try {
            jwtUtil.isExpired(accessToken);
        }
        catch (ExpiredJwtException e) {
            log.warn("error log -> ExpiredJwtException");

            filterChain.doFilter(request, response);

            return;
        }

        log.info("accessToken -> {}", accessToken);

        //토큰에서 username과 role 획득
        Long username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        //userDTO를 생성하여 값 set
        MemberDto memberDto = MemberDto.builder()
            .username(username)
            .role(role)
            .build();

        log.info("유저 정보 세션 등록 -> " + username);

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(memberDto);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, customOAuth2User.getUserName(), customOAuth2User.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);



        filterChain.doFilter(request, response);
    }
}
