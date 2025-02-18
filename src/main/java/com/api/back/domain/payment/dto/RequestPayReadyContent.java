package com.api.back.domain.payment.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class RequestPayReadyContent {

    @Builder.Default
    String cid = "TC0ONETIME"; //가맹점 코드, 10자 테스트 환경 : TC0ONETIME

    UUID partner_order_id; //주문번호
    String partner_user_id; //회원번호
    String item_name; //상품명, 최대 100자
    int quantity; //상품 수량
    int total_amount; //상품 총액
    int tax_free_amount; //상품 비과세 금액
    String approval_url; //결제 성공 시 redirect url, 최대 255자
    String cancel_url; //결제 취소 시 redirect url, 최대 255자
    String fail_url; //결제 실패 시 redirect url, 최대 255자
    @Builder.Default
    boolean tms_result = true;
}