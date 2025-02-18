package com.api.back.domain.reservation.type;

public enum ReservationStatusRequest {
    PAYMENT_PENDING, // 결제대기
    RESERVATION_COMPLETED, // 예약완료
    CONSULTING_COMPLETED, // 상담완료
    CANCELLED // 예약취소
}
