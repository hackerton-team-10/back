package com.api.back.domain.payment.application;

import com.api.back.domain.payment.dto.response.ResponseAccountPayContent;

public interface PaymentService {

    public ResponseAccountPayContent postAccountPayment(Long memberId, Long reservationId, Integer fee);
}
