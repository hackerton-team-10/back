package com.api.back.domain.reservation.entity;

import com.api.back.domain.designer.dto.response.DesignerInfo;
import com.api.back.domain.member.domain.Member;
import com.api.back.domain.payment.entity.Payment;
import com.api.back.domain.payment.type.PaymentMethod;
import com.api.back.domain.reservation.dto.response.PaymentInfo;
import com.api.back.domain.reservation.dto.response.ReservationResponse;
import com.api.back.global.common.BaseEntity;
import com.api.back.domain.designer.entity.Designer;
import com.api.back.domain.reservation.type.ConsultationType;
import com.api.back.domain.reservation.type.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_id", nullable = false)
    private Designer designer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultationType consultationType;

    @Column(nullable = false)
    private Integer consultationFee;

    @Column(nullable = false)
    private LocalDateTime date;

    private String googleMeetLink;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    public ReservationResponse createReservationResponse(DesignerInfo designerInfo, PaymentInfo paymentInfo) {
        return ReservationResponse.builder()
                .reservationId(this.getId())
                .designer(designerInfo)
                .consultationType(this.getConsultationType())
                .date(this.getDate())
                .googleMeetLink(this.getGoogleMeetLink())
                .status(this.getStatus())
                .payment(paymentInfo)
                .build();
    }

    public ReservationResponse createReservationResponse(DesignerInfo designerInfo) {
        return ReservationResponse.builder()
            .reservationId(this.getId())
            .designer(designerInfo)
            .consultationType(this.getConsultationType())
            .date(this.getDate())
            .googleMeetLink(this.getGoogleMeetLink())
            .status(this.getStatus())
            .build();
    }

    public void updateStatus(ReservationStatus status) {this.status = status;}

    public void updatePayment(Payment payment) {this.payment = payment;}

    public void removeReservation(ReservationStatus status) {
        this.setRemovedDt(LocalDateTime.now());
        this.status = status;
    }

}