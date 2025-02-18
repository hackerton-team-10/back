package com.api.back.domain.payment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class RequestPayCancelContent {
    @Builder.Default
    String cid = "TC0ONETIME"; //가맹점 코드, 10자 테스트 환경 : TC0ONETIME
    private String tid; //결제 고유번호, 20자
    private int cancel_amount; //취소 금액
    private int cancel_tax_free_amount; //취소 비과세 금액
}
