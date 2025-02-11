package com.api.back.core.auth;

import com.api.back.core.enums.ResourceType;
import com.api.back.core.enums.UserType;
import com.api.back.core.service.SecurityManager;
import com.api.back.core.utils.JwtUtil;
import com.api.back.core.vo.AuthObject;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

import static com.api.back.core.enums.HeaderPrefix.AUTHORIZATION;
import static com.api.back.core.vo.AuthObject.getUnknownAuthObject;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final SecurityManager securityManager;

    @Value("${spring.profiles.active:local}")
    private String profile;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        boolean isMultipart = request.getContentType() != null && request.getContentType().startsWith("multipart/");
        HttpServletRequest wrapper = isMultipart ? request : new ContentCachingRequestWrapper(request);

        try {
            ResourceType resourceType = ResourceType.getResourceType(wrapper.getRequestURI());
            AuthObject authReq = getUnknownAuthObject();
            String accessToken = wrapper.getHeader(AUTHORIZATION.getPrefix());
            String uri = wrapper.getRequestURI();

            if (resourceType.isNeedToken() && accessToken != null) {
                authReq = handleAccessToken(accessToken);

                securityManager.setContextAuthentication(authReq);
            } else if (resourceType.isNeedAuthReq()) {
                securityManager.setContextAuthentication(authReq);
            }

            if (!isAuthorized(authReq.getUserType(), uri)) {
                throw new AccessDeniedException(String.format("%s 타입의 사용자에게 허용되지 않은 리소스입니다.", authReq.getUserType()));
            }
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                throw new IllegalArgumentException("토큰 에러: " + e.getMessage(), e);
            } else if (e instanceof ExpiredJwtException) {
                throw new ExpiredJwtException(null, null, "토큰이 만료되었습니다.");
            } else if (e instanceof MalformedJwtException) {
                throw new MalformedJwtException("잘못된 요청: " + e.getMessage());
            } else if (e instanceof AccessDeniedException) {
                throw new AccessDeniedException("권한 없음: " + e.getMessage());
            } else {
                throw new RuntimeException("서버 내부 오류", e);
            }
        }
        filterChain.doFilter(wrapper, response);
    }

    private boolean isAuthorized(UserType userType, String requestURI) {
        return switch (userType) {
            case ADMIN -> true;
            case CUSTOMER -> requestURI.startsWith(UserType.CUSTOMER.getRequestMapper()) ||
                    requestURI.startsWith(UserType.UNKNOWN.getRequestMapper());
            case UNKNOWN -> requestURI.startsWith(UserType.UNKNOWN.getRequestMapper());
        };
    }


    private AuthObject handleAccessToken(String accessToken) {

       /* if (!"test".equals(profile)) {
            if (jwtUtil.isTokenBlacklisted(accessToken)) {
                throw new AccessDeniedException("로그아웃된 유저 입니다");
            }
        }*/
        AuthObject authObj = jwtUtil.getUserInfoByToken(accessToken);
        return authObj;
    }


    //TODO RENEW TOKEN BLACKLIST
/*


private AuthObject validateAndRenewAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(REFRESHTOKEN.getPrefix());
        if (refreshToken == null) {
            throw new AccessDeniedException("로그인이 필요 합니다");
        }
        jwtUtil.existAndUpdateDateRefreshToken(refreshToken);
        AuthObject authUser = jwtUtil.getUserInfoByToken(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(authUser);
        response.addHeader(AUTHORIZATION.getPrefix(), newAccessToken);
        return authUser;
    }
*/

}
