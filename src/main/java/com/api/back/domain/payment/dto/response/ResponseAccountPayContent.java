package com.api.back.domain.payment.dto.response;

import com.api.back.domain.payment.type.PaymentMethod;
import com.api.back.domain.payment.type.PaymentStatus;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ResponseAccountPayContent {

    private UUID id;

    private Integer fee;

    private PaymentMethod method;

    private PaymentStatus status;

    private String bankType;

    private String account;
}
