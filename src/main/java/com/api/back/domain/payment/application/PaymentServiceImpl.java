package com.api.back.domain.payment.application;

import com.api.back.domain.member.domain.Member;
import com.api.back.domain.member.repository.MemberRepository;
import com.api.back.domain.payment.dto.response.ResponseAccountPayContent;
import com.api.back.domain.payment.entity.Payment;
import com.api.back.domain.payment.repository.PaymentRepository;
import com.api.back.domain.payment.type.PaymentMethod;
import com.api.back.domain.payment.type.PaymentStatus;
import com.api.back.domain.reservation.entity.Reservation;
import com.api.back.domain.reservation.repository.ReservationRepository;
import com.api.back.domain.reservation.type.ReservationStatus;
import com.api.back.global.common.response.SuccessType;
import com.api.back.global.common.response.WrapResponse;
import com.api.back.global.error.exception.BusinessLogicException;
import com.api.back.global.error.exception.ErrorCode;
import com.api.back.global.error.exception.ForbiddenException;
import com.api.back.global.error.exception.InvalidValueException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService{

    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    @Override
    public ResponseAccountPayContent postAccountPayment(Long memberId, Long reservationId, Integer fee) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessLogicException(
            ErrorCode.MEMBER_NOT_FOUND));

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new BusinessLogicException(
            ErrorCode.RESERVATION_NOT_FOUND
        ));

        // 예약자 ID와 요청자 ID가 같은지 확인
        if (!reservation.getMember().getId().equals(memberId)) {
            throw new ForbiddenException("자신의 예약이 아닙니다");
        }

        // 결제정보가 이미 있는 경우
        if(reservation.getPayment() != null) {
            throw new InvalidValueException(ErrorCode.RESERVATION_ALREADY_PAYMENT);
        }

        reservation.updateStatus(ReservationStatus.PAYMENT_PENDING);

        UUID orderId = UUID.randomUUID();

        Boolean isExist = paymentRepository.existsById(orderId);

        if(isExist) throw new BusinessLogicException(ErrorCode.PAYMENT_ALREADY_EXIST);

        Payment payment = paymentRepository.save(Payment.builder()
            .id(orderId)
            .member(member)
            .fee(fee)
            .method(PaymentMethod.CASH)
            .status(PaymentStatus.PENDING)
            .build());

        //결제 정보 저장
        reservation.updatePayment(payment);

        ResponseAccountPayContent response = payment.createAccountPayContent();
        response.setBankType("국민은행");
        response.setAccount("1234-567-2345-1122");

        return response;
    }
}
