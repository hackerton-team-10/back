package com.api.back.domain.payment.exception;

import com.api.back.global.error.exception.BusinessLogicException;
import com.api.back.global.error.exception.ErrorCode;

public class PaymentInfoNotFound extends BusinessLogicException {

    public PaymentInfoNotFound() {
        super(ErrorCode.PAYMENT_NOT_FOUND);
    }
}
