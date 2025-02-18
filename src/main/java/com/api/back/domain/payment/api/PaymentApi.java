package com.api.back.domain.payment.api;

import com.api.back.domain.member.domain.Member;
import com.api.back.domain.member.exception.MemberNotFoundException;
import com.api.back.domain.member.repository.MemberRepository;
import com.api.back.domain.payment.dto.RequestPayApproveContent;
import com.api.back.domain.payment.dto.RequestPayCancelContent;
import com.api.back.domain.payment.dto.RequestPayReadyContent;
import com.api.back.domain.payment.dto.response.ResponsePayApproveContent;
import com.api.back.domain.payment.dto.response.ResponsePayReadyContent;
import com.api.back.domain.payment.entity.Payment;
import com.api.back.domain.payment.exception.PaymentInfoNotFound;
import com.api.back.domain.payment.repository.PaymentRepository;
import com.api.back.domain.payment.type.PaymentMethod;
import com.api.back.domain.payment.type.PaymentStatus;
import com.api.back.domain.reservation.entity.Reservation;
import com.api.back.domain.reservation.exception.ReservationNotFoundException;
import com.api.back.domain.reservation.repository.ReservationRepository;
import com.api.back.domain.reservation.type.ReservationStatus;
import com.api.back.global.common.response.SuccessType;
import com.api.back.global.common.response.WrapResponse;
import com.api.back.global.config.security.dto.CustomOAuth2User;
import com.api.back.global.util.kakao.KaKaoPayUtil;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentApi implements PaymentApiDocs{

    @Value("${kakao.approval.url}")
    private String approvalUrl;

    @Value("${kakao.cancel.url}")
    private String cancellUrl;

    @Value("${kakao.fail.url}")
    private String failUrl;

    private final KaKaoPayUtil kaKaoPayUtil;

    private final MemberRepository memberRepository;

    private final PaymentRepository paymentRepository;

    private final ReservationRepository reservationRepository;

    /**
     * 결제 준비 요청
     * **/
    @GetMapping({""})
    @Transactional
    public ResponseEntity<WrapResponse<ResponsePayReadyContent>> requestPayUrl(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestParam("itemName") String itemName,
        @RequestParam("totalAmount") int totalAmount,
        @RequestParam("taxFreeAmount") int taxFreeAmount,
        @RequestParam("reservationId") Long reservationId
        ) {

        Member member = memberRepository.findById(customOAuth2User.getUserName()).orElseThrow(MemberNotFoundException::new);

        UUID orderId = UUID.randomUUID();

        approvalUrl += "?orderId=" + orderId;

        RequestPayReadyContent request = RequestPayReadyContent.builder()
            .partner_order_id(orderId)
            .partner_user_id(Long.toString(customOAuth2User.getUserName()))
            .item_name(itemName)
            .quantity(1)
            .total_amount(totalAmount)
            .tax_free_amount(taxFreeAmount)
            .approval_url(approvalUrl)
            .cancel_url(cancellUrl)
            .fail_url(failUrl)
            .build();

        log.info("결제 요청 -> {}", request.toString());

        ResponseEntity<ResponsePayReadyContent> response = kaKaoPayUtil.kakaoPayReadyCall(request);

        String tid = Objects.requireNonNull(response.getBody(), "tid must not be null").getTid();

        //TODO : payment 엔티티 DB 저장 로직
        Payment payment = paymentRepository.save(Payment.builder()
                .id(orderId)
                .paymentId(tid)
                .member(member)
                .fee(totalAmount)
                .method(PaymentMethod.KAKAOPAY)
                .status(PaymentStatus.PENDING)
            .build());

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFoundException::new);

        reservation.updatePayment(payment);

        return ResponseEntity.ok(WrapResponse.create(response.getBody(), SuccessType.SIMPLE_STATUS));
    }

    /**
     * 결제 취소 요청
     * **/
    @GetMapping("/cancel")
    @Transactional
    public ResponseEntity<WrapResponse<?>> approveProcess(
        @RequestParam("orderId") UUID orderId,
        @RequestParam("reservationId") Long reservationId
    ) {

        Payment payment = paymentRepository.findById(orderId).orElseThrow(PaymentInfoNotFound::new);

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
            ReservationNotFoundException::new);

        RequestPayCancelContent request = RequestPayCancelContent.builder()
            .tid(payment.getPaymentId())
            .cancel_amount(payment.getFee())
            .cancel_tax_free_amount(payment.getFee())
            .build();


        ResponseEntity<String> response = kaKaoPayUtil.kakaoPayCancelCall(request, reservationId);

        //결제 및 예약 상태 변경
        if(response.getStatusCode() == HttpStatus.OK) {

            reservation.updateStatus(ReservationStatus.CANCELLED);  //더티 체킹 확인
            payment.updatePaymentStatus(PaymentStatus.REFUND);
        }

        return ResponseEntity.ok(WrapResponse.create(response.getBody(), SuccessType.SIMPLE_STATUS));
    }

    /**
     * 결제 승인 요청
     * **/
    @GetMapping("/callback")
    @Transactional
    public ResponseEntity<WrapResponse<ResponsePayApproveContent>> approveProcess(
        @RequestParam("orderId") UUID orderId,
        @RequestParam("pg_token") String pgToken,
        @RequestParam("reservationId") Long reservationId
    ) {

        log.info("order id -> {}", orderId);
        log.info("pg token -> {}", pgToken);
        Payment paymentInfo = paymentRepository.findById(orderId).orElseThrow(PaymentInfoNotFound::new);


        RequestPayApproveContent request = RequestPayApproveContent.builder()
            .tid(paymentInfo.getPaymentId())
            .partner_order_id(orderId)
            .partner_user_id(String.valueOf(paymentInfo.getMember().getId()))
            .pg_token(pgToken)
            .build();

        ResponseEntity<ResponsePayApproveContent> response = kaKaoPayUtil.kakaoPayApproveCall(request);

        // 결제 및 예약 상태 변경
        if(response.getStatusCode() == HttpStatus.OK) {
            paymentInfo.updatePaymentStatus(PaymentStatus.COMPLETED);

            Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                ReservationNotFoundException::new);

            reservation.updateStatus(ReservationStatus.RESERVATION_COMPLETED);
        }

        return ResponseEntity.ok(WrapResponse.create(response.getBody(), SuccessType.SIMPLE_STATUS));
    }
}
