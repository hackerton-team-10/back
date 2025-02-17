package com.api.back.global.common;

import com.api.back.domain.member.repository.MemberRepository;
import com.api.back.global.common.response.SuccessType;
import com.api.back.global.common.response.WrapResponse;
import com.api.back.global.config.security.jwt.JWTUtil;
import com.api.back.global.error.exception.ErrorCode;
import com.api.back.global.error.exception.InvalidValueException;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.Cookie;

@RestController
@RequestMapping("/v1")
@Tag(name = "Open API (no auth)")
public class CommonController {

    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    public CommonController(JWTUtil jwtUtil, MemberRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/health")
    @Operation(summary = "Health Check (0)", description = "Health Check API")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json", schema = @Schema(type = "boolean"))})
    public boolean healthCheck() {
        return true;
    }

    @GetMapping("/dev/token")
    @Operation(summary = "개발용 토큰 발급 엔드포인트", description = "개발용 3시간 AccessToken 발급")
    @Parameter(name = "name", required = true, description = "사용자 이름")
    @ApiResponse(responseCode = "201", description = "Success", content = {@Content(mediaType = "application/json", schema = @Schema(type = "String"))})
    public ResponseEntity<WrapResponse<String>> getDevToken(@RequestParam("name") String name) {

        String response = jwtUtil.createJwt("access", name, "ROLE_USER", 1000 * 60 * 60 * 3L);

        return ResponseEntity.ok(WrapResponse.create(response, SuccessType.STATUS_201));
    }

    @PostMapping("/reissue")
    @Operation(summary = "리프레시 토큰을 통한 액세스 토큰 재발급 엔드포인트", description = "쿠키에 refreshToken 담아서 요청보내야 합니다.")
    @ApiResponse(responseCode = "201", description = "Header로 액세스 토큰 발급", content = {@Content(mediaType = "application/json", schema = @Schema(type = "String"))})
    @ApiResponse(responseCode = "400", description = "refreshToken missing 및 expired 시 응답", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = InvalidValueException.class))})
    public ResponseEntity<WrapResponse<?>> reissue(HttpServletRequest request, HttpServletResponse response) {

        //get refresh token
        String Authorization = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("Authorization")) {

                Authorization = cookie.getValue();
            }
        }

        if (Authorization == null) {

            //response status code
            throw new InvalidValueException(ErrorCode.INVALID_REQUEST_PARAM);
        }

        //expired check
        try {
            jwtUtil.isExpired(Authorization);
        } catch (ExpiredJwtException e) {

            //response status code
            throw new InvalidValueException(ErrorCode.REFRESHTOKEN_EXPIRED);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(Authorization);

        if (!category.equals("refresh")) {

            //response status code
            throw new InvalidValueException(ErrorCode.INVALID_REQUEST_PARAM);
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = memberRepository.existsByRefreshToken(Authorization);
        if (!isExist) {

            //response body
            throw new InvalidValueException(ErrorCode.REFRESHTOKEN_INVALID);
        }

        String userName = jwtUtil.getUsername(Authorization);
        String role = jwtUtil.getRole(Authorization);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", userName, role, 600000L);
//        String newRefresh = jwtUtil.createJwt("refresh", userName, role, 86400000L);

        // TODO : RefreshToken Rotate 추가를 위한 DB 갱신 로직 작성

        //response
        response.setHeader("Authorization", newAccess);
//        response.addCookie(createCookie("refresh", newRefresh));

        return ResponseEntity.ok(WrapResponse.create("액세스 토큰 생성 완료", SuccessType.STATUS_201));
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
