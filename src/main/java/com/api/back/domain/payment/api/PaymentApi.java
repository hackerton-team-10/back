package com.api.back.domain.payment.api;

import com.api.back.domain.member.domain.Member;
import com.api.back.domain.member.exception.MemberNotFoundException;
import com.api.back.domain.member.repository.MemberRepository;
import com.api.back.domain.payment.dto.RequestPayApproveContent;
import com.api.back.domain.payment.dto.RequestPayReadyContent;
import com.api.back.domain.payment.dto.response.ResponsePayApproveContent;
import com.api.back.domain.payment.dto.response.ResponsePayReadyContent;
import com.api.back.domain.payment.entity.Payment;
import com.api.back.domain.payment.exception.PaymentInfoNotFound;
import com.api.back.domain.payment.repository.PaymentRepository;
import com.api.back.domain.payment.type.PaymentMethod;
import com.api.back.domain.payment.type.PaymentStatus;
import com.api.back.global.common.response.SuccessType;
import com.api.back.global.common.response.WrapResponse;
import com.api.back.global.config.security.dto.CustomOAuth2User;
import com.api.back.global.util.kakao.KaKaoPayUtil;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    /**
     * 결제 준비 요청
     * **/
    @GetMapping({""})
    public ResponseEntity<WrapResponse<ResponsePayReadyContent>> requestPayUrl(
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
        @RequestParam("itemName") String itemName,
        @RequestParam("totalAmount") int totalAmount,
        @RequestParam("taxFreeAmount") int taxFreeAmount
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
        paymentRepository.save(Payment.builder()
                .id(orderId)
                .paymentId(tid)
                .member(member)
                .fee(totalAmount)
                .method(PaymentMethod.KAKAOPAY)
                .status(PaymentStatus.PENDING)
            .build());

        return ResponseEntity.ok(WrapResponse.create(response.getBody(), SuccessType.SIMPLE_STATUS));
    }

    /**
     * 결제 승인 요청
     * **/
    @GetMapping("/callback")
    public ResponseEntity<WrapResponse<ResponsePayApproveContent>> approveProcess(
        @RequestParam("orderId") UUID orderId,
        @RequestParam("pg_token") String pgToken
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

        System.out.println(response.getBody());
        return ResponseEntity.ok(WrapResponse.create(response.getBody(), SuccessType.SIMPLE_STATUS));
    }

    /**
     * 결제 승인 요청
     * **/
//    @PutMapping("/cancel")
//    public ResponseEntity<WrapResponse<?>> approveProcess(
//        @RequestParam("paymentId") String paymentId,
//        @RequestParam("fee") int fee
//    ) {
//
////        ResponseEntity<ResponsePayApproveContent> response = kaKaoPayUtil.kakaoPayApproveCall(request);
//
//        System.out.println(response.getBody());
//        return ResponseEntity.ok(WrapResponse.create(response.getBody(), SuccessType.SIMPLE_STATUS));
//    }
}
