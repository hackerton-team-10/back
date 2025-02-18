package com.api.back.domain.payment.api;

import com.api.back.domain.payment.dto.response.ResponsePayApproveContent;
import com.api.back.domain.payment.dto.response.ResponsePayReadyContent;
import com.api.back.global.common.response.SuccessType;
import com.api.back.global.common.response.WrapResponse;
import com.api.back.global.config.security.dto.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name ="결제 모듈", description = "결제 관련 API 문서")
public interface PaymentApiDocs {

    @Operation(summary = "결제 요청 API",description = "결제 요청을 위한 엔드포인트입니다.")
    @ApiResponse(responseCode = "200", description = "next_redirect_pc_url값 사용하시면 될 것 같습니다.", content = @Content(schema = @Schema(implementation = ResponsePayReadyContent.class)))
    @Parameters(value = {
        @Parameter(name = "itemName", description = "현재 예약하려는 상품의 이름"),
        @Parameter(name = "totalAmount", description = "총 금액"),
        @Parameter(name = "taxFreeAmount", description = "총 금액과 동일하게 보내주시면 됩니다.")
    })
    @GetMapping({""})
    public ResponseEntity<WrapResponse<ResponsePayReadyContent>> requestPayUrl(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestParam("itemName") String itemName,
        @RequestParam("totalAmount") int totalAmount,
        @RequestParam("taxFreeAmount") int taxFreeAmount
    );

    @Operation(summary = "결제 승인 요청 API",description = "결제 성공 이후 카카오 서버로 승인 요청을 위한 엔드포인트입니다.")
    @ApiResponse(responseCode = "200", description = "결제에 대한 정보만 있습니다.", content = @Content(schema = @Schema(implementation = ResponsePayApproveContent.class)))
    @Parameters(value = {
        @Parameter(name = "orderId", description = "주문번호 / 리디렉션 uri에서 가져오시면 됩니다."),
        @Parameter(name = "pg_token", description = "결제 정보 토큰 / 리디렉션 uri에서 가져오시면 됩니다.")
    })
    @GetMapping("/callback")
    public ResponseEntity<WrapResponse<ResponsePayApproveContent>> approveProcess(
        @RequestParam("orderId") UUID orderId,
        @RequestParam("pg_token") String pgToken
    );
}
