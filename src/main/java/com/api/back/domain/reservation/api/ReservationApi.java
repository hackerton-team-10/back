package com.api.back.domain.reservation.api;

import com.api.back.domain.payment.type.PaymentMethod;
import com.api.back.domain.reservation.application.ReservationService;
import com.api.back.domain.reservation.dto.response.ReservationResponse;
import com.api.back.domain.reservation.type.ConsultationType;
import com.api.back.domain.reservation.type.ReservationStatusRequest;
import com.api.back.global.common.response.SuccessType;
import com.api.back.global.common.response.WrapResponse;
import com.api.back.global.config.security.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationApi implements ReservationApiDocs {
    private final ReservationService reservationService;

    @Override
    @GetMapping({""})
    public ResponseEntity<WrapResponse<List<ReservationResponse>>> reservationList(ReservationStatusRequest status) {
        List<ReservationResponse> response = reservationService.getReservationList(status);
        return ResponseEntity.ok(WrapResponse.create(response, SuccessType.SIMPLE_STATUS));
    }

    @Override
    @GetMapping({"/{reservationId}"})
    public ResponseEntity<WrapResponse<ReservationResponse>> reservation(Long reservationId) {
        ReservationResponse response = reservationService.getReservation(reservationId);
        return ResponseEntity.ok(WrapResponse.create(response, SuccessType.SIMPLE_STATUS));
    }

    @Override
    @PostMapping({""})
    public ResponseEntity<WrapResponse<ReservationResponse>> postReservation(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, Long designerId, LocalDateTime date, ConsultationType consultationType) {
        ReservationResponse response = reservationService.postReservation(customOAuth2User.getUserName(), designerId, date, consultationType);
        return ResponseEntity.ok(WrapResponse.create(response, SuccessType.SIMPLE_STATUS));
    }
}
