package com.api.back.domain.payment.type;

public enum PaymentStatus {
    PENDING, // 결제대기
    COMPLETED, // 결제완료
    CANCELLED, // 결제취소
    REFUND // 환불완료
}
