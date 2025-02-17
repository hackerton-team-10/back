package com.api.back.domain.reservation.dto.response;

import com.api.back.domain.designer.dto.response.DesignerInfo;
import com.api.back.domain.reservation.type.ConsultationType;
import com.api.back.domain.reservation.type.ReservationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ReservationResponse {
    Long reservationId;
    DesignerInfo designer;
    ConsultationType consultationType;
    LocalDateTime date;
    String googleMeetLink;
    ReservationStatus status;
    PaymentInfo payment;
}
