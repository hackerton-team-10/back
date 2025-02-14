package com.api.back.domain.member.api;

import com.api.back.domain.member.dto.response.MemberResponse;
import com.api.back.global.common.response.WrapResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Swagger 문서 작업을 위한 추상 인터페이스
 * Controller 코드를 간결화하기 위해 해당 인터페이스에서 어노테이션 작업을 수행
 * **/
@Tag(name ="유저 API ", description = "유저 API")
public interface MemberApiDocs {

    @Operation(summary = "Init 엔드포인트",description = "프로젝트 세팅용 엔드포인트입니다.")
    @Parameter(name = "memberId", required = false, description = "찾을 유저의 ID값")
    @ApiResponse(responseCode = "200", description = "MemberResponse", content = @Content(schema = @Schema(implementation = MemberResponse.class)))
    @GetMapping({"/{memberId}", "/"})
    public ResponseEntity<WrapResponse<MemberResponse>> memberP(
        @PathVariable(value = "memberId", required = false) Optional<Long> id);
}
