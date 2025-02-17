package com.api.back.domain.reservation.exception;

import com.api.back.global.error.exception.BusinessLogicException;
import com.api.back.global.error.exception.ErrorCode;

public class ReservationNotFoundException extends BusinessLogicException {
    public ReservationNotFoundException() {
        super(ErrorCode.RESERVATION_NOT_FOUND);
    }
}