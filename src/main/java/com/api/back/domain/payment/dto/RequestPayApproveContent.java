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
public class RequestPayApproveContent {

    @Builder.Default
    String cid = "TC0ONETIME"; //가맹점 코드, 10자 테스트 환경 : TC0ONETIME
    String tid; //결제 고유번호
    UUID partner_order_id; //주문번호
    String partner_user_id; //회원번호
    String pg_token; //결제 승인 요청 코드
}
