package com.api.back.domain.reservation.application;

import com.api.back.domain.designer.dto.response.DesignerInfo;
import com.api.back.domain.designer.entity.Designer;
import com.api.back.domain.designer.exception.DesignerNotFoundException;
import com.api.back.domain.designer.repository.DesignerRepository;
import com.api.back.domain.member.domain.Member;
import com.api.back.domain.member.exception.MemberNotFoundException;
import com.api.back.domain.member.repository.MemberRepository;
import com.api.back.domain.payment.entity.Payment;
import com.api.back.domain.payment.repository.PaymentRepository;
import com.api.back.domain.reservation.dto.response.PaymentInfo;
import com.api.back.domain.reservation.dto.response.ReservationResponse;
import com.api.back.domain.reservation.entity.Reservation;
import com.api.back.domain.reservation.exception.ReservationNotAvailableException;
import com.api.back.domain.reservation.repository.ReservationRepository;
import com.api.back.domain.reservation.type.ConsultationType;
import com.api.back.domain.reservation.type.ReservationStatus;
import com.api.back.domain.reservation.type.ReservationStatusRequest;
import com.api.back.global.common.response.WrapResponse;
import com.api.back.global.config.security.dto.CustomOAuth2User;
import com.api.back.global.error.exception.ForbiddenException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final DesignerRepository designerRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public List<ReservationResponse> getReservationList(Long memberId, ReservationStatusRequest reservationStatusRequest) {
        ReservationStatus reservationStatus = switch (reservationStatusRequest) {
            case PAYMENT_PENDING -> ReservationStatus.PAYMENT_PENDING;
            case CANCELLED -> ReservationStatus.CANCELLED;
            default -> ReservationStatus.RESERVATION_COMPLETED;
        };

        List<ReservationResponse> response = reservationRepository.findAllByMemberIdAndStatus(memberId, reservationStatus)
            .stream()
            .map(reservation -> {
                DesignerInfo designerInfo = Optional.ofNullable(reservation.getDesigner())
                    .map(designer -> designerRepository.findById(designer.getId()).orElse(null))
                    .map(Designer::createDesignerInfo)
                    .orElse(null);

                PaymentInfo paymentInfo = Optional.ofNullable(reservation.getPayment())
                    .map(payment -> paymentRepository.findById(payment.getId()).orElse(null))
                    .map(Payment::createPaymentInfo)
                    .orElse(null);

                return reservation.createReservationResponse(designerInfo, paymentInfo);
            })
            .toList();


        LocalDateTime now = LocalDateTime.now();
        if(reservationStatusRequest.equals(ReservationStatusRequest.CONSULTING_COMPLETED)){
            // 상담 완료 요청일 경우, 예약 시간이 지난 데이터만 내려주기
            return response.stream().filter(v->now.isAfter(v.getDate())).toList();
        }
        if(reservationStatusRequest.equals(ReservationStatusRequest.RESERVATION_COMPLETED)){
            // 예약 완료 요청(상담 전)일 경우, 예약 시간이 지나지 않은 데이터만 내려주기
            return response.stream().filter(v->now.isBefore(v.getDate())).toList();
        }

        return response;
    }

    @Override
    public List<ReservationResponse> getReservationAllList(
        Long memberId) {

        List<ReservationResponse> response = reservationRepository.findAllByMemberId(memberId)
            .stream()
            .map(reservation -> {
                DesignerInfo designerInfo = Optional.ofNullable(reservation.getDesigner())
                    .map(designer -> designerRepository.findById(designer.getId()).orElse(null))
                    .map(Designer::createDesignerInfo)
                    .orElse(null);

                PaymentInfo paymentInfo = Optional.ofNullable(reservation.getPayment())
                    .map(payment -> paymentRepository.findById(payment.getId()).orElse(null))
                    .map(Payment::createPaymentInfo)
                    .orElse(null);

                return reservation.createReservationResponse(designerInfo, paymentInfo);
            })
            .toList();

        return response;
    }

    @Override
    public ReservationResponse getReservation(Long memberId, Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(MemberNotFoundException::new);

        // 예약자 ID와 요청자 ID가 같은지 확인
        if (!reservation.getMember().getId().equals(memberId)) {
            throw new ForbiddenException("자신의 예약이 아닙니다");
        }

        DesignerInfo designerInfo = designerRepository.findById(reservation.getDesigner().getId()).orElse(null).createDesignerInfo();
        if(reservation.getPayment() == null) {
            return reservation.createReservationResponse(designerInfo, null);
        }
        PaymentInfo paymentInfo = paymentRepository.findById(reservation.getPayment().getId()).orElse(null).createPaymentInfo();

        return reservation.createReservationResponse(designerInfo, paymentInfo);
    }

    @Override
    @Transactional
    public ReservationResponse postReservation(Long memberId, Long designerId, LocalDateTime date, ConsultationType consultationType) {

        // 해당 날짜 예약 가능 여부 확인
        if (!isAvailableReservation(designerId, date)) {
            throw new ReservationNotAvailableException();
        }

        Member member = memberRepository.getReferenceById(memberId);
        Designer designer = designerRepository.findById(designerId).orElseThrow(DesignerNotFoundException::new);

        // 예약하기
        Reservation reservation = reservationRepository.save(Reservation.builder()
                .member(member)
                .designer(designer)
                .consultationType(consultationType)
                .consultationFee(consultationType.equals(ConsultationType.IN_PERSON) ? designer.getConsultingFeeInPerson() : designer.getConsultingFeeVideo())
                .date(date)
                .status(ReservationStatus.PAYMENT_PENDING)
                .build());

        DesignerInfo designerInfo = designer.createDesignerInfo();

        return reservation.createReservationResponse(designerInfo);
    }

    @Transactional
    @Override
    public void postCancelReservation(Long memberId, Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(MemberNotFoundException::new);

        // 예약자 ID와 요청자 ID가 같은지 확인
        if (!reservation.getMember().getId().equals(memberId)) {
            throw new ForbiddenException("자신의 예약이 아닙니다");
        }

        reservation.removeReservation(ReservationStatus.CANCELLED);

    }

    private Boolean isAvailableReservation(Long designerId, LocalDateTime date) {
        if (LocalDateTime.now().isAfter(date)) return false;
        return reservationRepository.findAllByDesignerIdAndDate(designerId, date).isEmpty();
    }

    // TODO: google meet link 생성 로직 구현 필요
    private String createGoogleMeetLink() {
        return "1234";
    }
}
