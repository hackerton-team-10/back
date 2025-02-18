package com.api.back.domain.payment.entity;

import com.api.back.domain.member.domain.Member;
import com.api.back.domain.reservation.dto.response.PaymentInfo;
import com.api.back.global.common.BaseEntity;
import com.api.back.domain.payment.type.PaymentMethod;
import com.api.back.domain.payment.type.PaymentStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {
    @Id
    private UUID id;

    @Nullable // 카카오페이로 결제할 때만 해당 값 필요
    private String paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Integer fee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    public PaymentInfo createPaymentInfo() {

        return PaymentInfo.builder()
                .id(this.getId())
                .fee(this.getFee())
                .method(this.getMethod())
                .build();
    }

    public void updatePaymentStatus(PaymentStatus status) {this.status = status;}
}
