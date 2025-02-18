package com.api.back.domain.reservation.api;

import com.api.back.domain.payment.type.PaymentMethod;
import com.api.back.domain.reservation.dto.response.ReservationResponse;
import com.api.back.domain.reservation.type.ConsultationType;
import com.api.back.domain.reservation.type.ReservationStatusRequest;
import com.api.back.global.common.response.WrapResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "예약 API", description = "예약 API")
public interface ReservationApiDocs {
    @Operation(summary = "예약 리스트 조회")
    @GetMapping({""})
    public ResponseEntity<WrapResponse<List<ReservationResponse>>> reservationList(
            @RequestParam
            ReservationStatusRequest status
    );

    @Operation(summary = "예약 조회")
    @GetMapping({"/{reservationId}"})
    public ResponseEntity<WrapResponse<ReservationResponse>> reservation(
            @PathVariable(value = "reservationId", required = true) Long reservationId
    );

    @Operation(summary = "예약하기")
    @PostMapping({""})
    public ResponseEntity<WrapResponse<ReservationResponse>> postReservation(
            @RequestParam
            Long designerId,
            @RequestParam
            LocalDateTime date,
            @RequestParam
            PaymentMethod paymentMethod,
            @RequestParam
            ConsultationType consultationType
    );
}
