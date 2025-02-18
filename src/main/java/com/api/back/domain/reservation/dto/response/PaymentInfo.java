package com.api.back.domain.reservation.dto.response;

import com.api.back.domain.payment.type.PaymentMethod;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentInfo {
    UUID id;
    Integer fee;
    String paymentId;
    PaymentMethod method;
}
