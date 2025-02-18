package com.api.back.global.util.kakao;

import com.api.back.domain.payment.dto.RequestPayApproveContent;
import com.api.back.domain.payment.dto.RequestPayCancelContent;
import com.api.back.domain.payment.dto.RequestPayReadyContent;
import com.api.back.domain.payment.dto.response.ResponsePayApproveContent;
import com.api.back.domain.payment.dto.response.ResponsePayReadyContent;
import com.api.back.domain.payment.entity.Payment;
import com.api.back.domain.payment.repository.PaymentRepository;
import com.api.back.domain.reservation.repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class KaKaoPayUtil {

    @Value("${kakao.secret.key}")
    private String secretKey;

    @Value("${kakao.ready.url}")
    private String readyUrl;

    @Value("${kakao.pay-cancel.url}")
    private String cancelUrl;

    @Value("${kakao.approve.url}")
    private String approveUrl;

    private final RestTemplate restTemplate;

    private final PaymentRepository paymentRepository;

    private final ReservationRepository reservationRepository;



    public KaKaoPayUtil(RestTemplate restTemplate, PaymentRepository paymentRepository, ReservationRepository reservationRepository) {
        this.restTemplate = restTemplate;
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
    }

    public ResponseEntity<ResponsePayReadyContent> kakaoPayReadyCall(
        RequestPayReadyContent request) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", secretKey);    //DEV Secret Key
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RequestPayReadyContent> entity = new HttpEntity<>(request, headers);


        return restTemplate.postForEntity(readyUrl, entity, ResponsePayReadyContent.class);
    }

    public ResponseEntity<ResponsePayApproveContent> kakaoPayApproveCall(RequestPayApproveContent request) {

        log.info(request.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", secretKey);    //DEV Secret Key
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RequestPayApproveContent> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ResponsePayApproveContent> response = restTemplate.postForEntity(approveUrl, entity, ResponsePayApproveContent.class);

        return response;
    }

    // TODO : 취소 엔드포인트 작성 필요
    public ResponseEntity<?> kakaoPayCancelCall(RequestPayCancelContent request) {

        log.info(request.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", secretKey);    //DEV Secret Key
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RequestPayCancelContent> entity = new HttpEntity<>(request, headers);

        ResponseEntity<?> response = restTemplate.postForEntity(cancelUrl, entity, ResponsePayApproveContent.class);

        //TODO : payment 테이블 status pending -> REFUND 변경, reservation status COMPLITED -> CANCELLED 변경
//        Payment payment = paymentRepository.findByPaymentId()

        return response;
    }


}
