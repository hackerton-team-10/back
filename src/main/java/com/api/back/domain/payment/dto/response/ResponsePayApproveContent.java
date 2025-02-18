package com.api.back.domain.payment.dto.response;

import com.api.back.domain.payment.type.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ResponsePayApproveContent {
    private String aid; //요청 고유 번호 - 승인/취소가 구분된 결제번호
    private String tid; //결제 고유 번호 - 승인/취소가 동일한 결제번호
    private String cid; //가맹점 코드 (고정)
    private String partner_order_id; //주문번호
    private String partner_user_id; //회원ID
    private String item_name; //상품명
    private int quantity; //수량
    private Amount amount; //가격

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime approvedAt;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Amount {
        private int total;
        private int taxFree;
        private int vat;
        private int point;
        private int discount;
        private int greenDeposit;
    }
}
