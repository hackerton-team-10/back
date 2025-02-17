package com.api.back.domain.reservation.dto.response;

import com.api.back.domain.payment.type.PaymentMethod;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentInfo {
    Long id;
    Integer fee;
    PaymentMethod method;
}
