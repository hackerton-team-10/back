package com.api.back.global.util.kakao;

import com.api.back.domain.payment.dto.RequestPayApproveContent;
import com.api.back.domain.payment.dto.RequestPayCancelContent;
import com.api.back.domain.payment.dto.RequestPayReadyContent;
import com.api.back.domain.payment.dto.response.ResponsePayApproveContent;
import com.api.back.domain.payment.dto.response.ResponsePayReadyContent;
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


    public KaKaoPayUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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

    public ResponseEntity<String> kakaoPayCancelCall(RequestPayCancelContent request, Long reservationId) {

        log.info(request.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", secretKey);    //DEV Secret Key
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RequestPayCancelContent> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(cancelUrl, entity, String.class);

        return response;
    }


}
