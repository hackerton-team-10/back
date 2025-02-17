package com.api.back.domain.reservation.exception;

import com.api.back.global.error.exception.BusinessLogicException;
import com.api.back.global.error.exception.ErrorCode;

public class ReservationNotAvailableException extends BusinessLogicException {
    public ReservationNotAvailableException() {
        super(ErrorCode.RESERVATION_NOT_AVAILABLE);
    }
}