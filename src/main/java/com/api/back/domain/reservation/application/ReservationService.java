package com.api.back.domain.reservation.application;

import com.api.back.domain.payment.type.PaymentMethod;
import com.api.back.domain.reservation.dto.response.ReservationResponse;
import com.api.back.domain.reservation.type.ConsultationType;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    List<ReservationResponse> getReservationList();
    ReservationResponse getReservation(Long reservationId);
    ReservationResponse postReservation(Long designerId, LocalDateTime date, PaymentMethod paymentMethod, ConsultationType consultationType);
}
