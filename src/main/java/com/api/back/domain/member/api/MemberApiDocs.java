package com.api.back.domain.member.api;

import com.api.back.domain.member.dto.response.MemberResponse;
import com.api.back.global.common.response.SuccessType;
import com.api.back.global.common.response.WrapResponse;
import com.api.back.global.config.security.dto.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Swagger 문서 작업을 위한 추상 인터페이스
 * Controller 코드를 간결화하기 위해 해당 인터페이스에서 어노테이션 작업을 수행
 * **/
@Tag(name ="유저 API ", description = "유저 API")
public interface MemberApiDocs {
    @Operation(summary = "사용자 정보 조회",description = "로그인한 사용자 정보 조회")
    @GetMapping("")
    public ResponseEntity<WrapResponse<MemberResponse>> member(@AuthenticationPrincipal CustomOAuth2User customOAuth2User);


    @Operation(summary = "사용자 이름 변경 메서드",description = "첫 로그인 시 유저이름 변경 메서드")
    @ApiResponse(responseCode = "204", description = "반환되는 값은 없습니다.", content = @Content(schema = @Schema(implementation = SuccessType.class)))
    @Parameter(name = "userName", description = "변경할 유저 이름")
    @PatchMapping("/")
    public ResponseEntity<WrapResponse<SuccessType>> memberP(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestParam("userName") String userName);
}
