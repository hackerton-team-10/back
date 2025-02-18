package com.api.back.domain.reservation.type;

public enum ReservationStatus {
    PAYMENT_PENDING, // 결제대기
    RESERVATION_COMPLETED, // 예약완료
    CANCELLED // 예약취소
}
