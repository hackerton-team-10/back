package com.api.back.domain.reservation.api;

import com.api.back.domain.payment.type.PaymentMethod;
import com.api.back.domain.reservation.application.ReservationService;
import com.api.back.domain.reservation.dto.response.ReservationResponse;
import com.api.back.domain.reservation.type.ConsultationType;
import com.api.back.domain.reservation.type.ReservationStatusRequest;
import com.api.back.global.common.response.SuccessType;
import com.api.back.global.common.response.WrapResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/{timeSlot}/hold")
    public ResponseEntity<WrapResponse<?>> postReserveTimeSlot(@PathVariable String timeSlot, @RequestParam String userId) {

        String key = "reservation:" + timeSlot;

        // TODO : Redis 예약 시간 중복 체크 및 정보 저장 (TTS : 15분, 결제 QR 타임아웃 = 15분)
//        // 이미 예약된 시간대라면 예약 불가
//        if (redisTemplate.hasKey(key)) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 예약된 시간대입니다.");
//        }
//
//        // Redis에 예약 정보 저장 (10분 동안만 보류)
//        redisTemplate.opsForValue().set(key, userId, Duration.ofMinutes(10));

        return ResponseEntity.ok(WrapResponse.create(SuccessType.STATUS_204));
    }

    @Override
    @PostMapping({""})
    public ResponseEntity<WrapResponse<ReservationResponse>> postReservation(Long designerId, LocalDateTime date, PaymentMethod paymentMethod, ConsultationType consultationType) {
        ReservationResponse response = reservationService.postReservation(designerId, date, paymentMethod, consultationType);
        return ResponseEntity.ok(WrapResponse.create(response, SuccessType.SIMPLE_STATUS));
    }
}
