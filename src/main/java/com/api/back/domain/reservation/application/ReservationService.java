package com.api.back.domain.reservation.application;

import com.api.back.domain.payment.type.PaymentMethod;
import com.api.back.domain.reservation.dto.response.ReservationResponse;
import com.api.back.domain.reservation.type.ConsultationType;
import com.api.back.domain.reservation.type.ReservationStatusRequest;

import com.api.back.global.common.response.WrapResponse;
import com.api.back.global.config.security.dto.CustomOAuth2User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface ReservationService {
    List<ReservationResponse> getReservationList(Long memberId, ReservationStatusRequest status);
    public List<ReservationResponse> getReservationAllList(Long memberId);
    ReservationResponse getReservation(Long memberId, Long reservationId);
    ReservationResponse postReservation(Long memberId, Long designerId, LocalDateTime date, ConsultationType consultationType);
    void postCancelReservation(Long memberId, Long reservationId);
}
