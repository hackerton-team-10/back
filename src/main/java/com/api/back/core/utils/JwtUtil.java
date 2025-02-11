package com.api.back.core.utils;

import com.api.back.core.enums.UserType;
import com.api.back.core.vo.AuthObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    //TODO
    //private final RefreshTokenRepository repository;
    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }


    public String generateAccessToken(AuthObject authUserObject) {
        long ACCESSEXPIREDTIME = 14 * 24 * 60 * 60 * 1000L;
        return Jwts.builder()
                .claim("userId", authUserObject.getUserId())
                .claim("userType", authUserObject.getUserType().name())
                .signWith(key)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESSEXPIREDTIME))
                .compact();
    }

    /**
     * Refresh Token 생성 (서버에서 관리)
     */
    public String generateRefreshToken(AuthObject authUserObject) {
        return Jwts.builder()
                .claim("userId", authUserObject.getUserId())
                .claim("userType", authUserObject.getUserType().name())
                .signWith(key)
                .compact();
    }

    public Claims validateAndGetClaims(String token) {
        try {
            return extractClaims(token);
        } catch (MalformedJwtException e) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        } catch (Exception e) {
            throw e;
        }
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("JWT 토큰이 만료되었습니다.");
        }
    }

    public AuthObject convertToRequest(String token) {
        Claims claims = validateAndGetClaims(token);
        return new AuthObject(claims.getId(),
                UserType.valueOf(claims.getSubject()
                )
        );
    }

    public AuthObject getUserInfoByToken(String token) {
        return convertToRequest(token);
    }
}