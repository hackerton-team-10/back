package com.api.back.domain.reservation.application;

import com.api.back.domain.payment.type.PaymentMethod;
import com.api.back.domain.reservation.dto.response.ReservationResponse;
import com.api.back.domain.reservation.type.ConsultationType;
import com.api.back.domain.reservation.type.ReservationStatusRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    List<ReservationResponse> getReservationList(ReservationStatusRequest status);
    ReservationResponse getReservation(Long reservationId);
    ReservationResponse postReservation(Long memberId, Long designerId, LocalDateTime date, PaymentMethod paymentMethod, ConsultationType consultationType);
}
