package com.api.back.domain.designer.application;

import com.api.back.domain.designer.dto.response.DesignerResponse;
import com.api.back.domain.designer.dto.response.DesignerTimesResponse;
import com.api.back.domain.designer.dto.response.TimeSlot;
import com.api.back.domain.designer.entity.Designer;
import com.api.back.domain.designer.repository.DesignerRepository;
import com.api.back.domain.designer.type.Region;
import com.api.back.domain.member.exception.MemberNotFoundException;
import com.api.back.domain.reservation.entity.Reservation;
import com.api.back.domain.reservation.repository.ReservationRepository;
import com.api.back.domain.reservation.type.ConsultationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class DesignerServiceImpl implements DesignerService {
    private final DesignerRepository designerRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public List<DesignerResponse> getDesignerList(Optional<Region> region, Optional<ConsultationType> consultationType, Optional<Integer> minFee, Optional<Integer> maxFee) {
        return designerRepository.findAllByFilters(region.orElse(null), consultationType.orElse(null), minFee.orElse(0), maxFee.orElse(2147483647)).stream().map(Designer::createDesignerResponse).toList();
    }

    @Override
    public DesignerResponse getDesigner(Long designerId) {
        return designerRepository.findById(designerId).orElseThrow(MemberNotFoundException::new).createDesignerResponse();
    }

    @Override
    public DesignerTimesResponse getDesignerTimes(Long designerId, LocalDate date) {
        List<Reservation> reservationList = reservationRepository.findAllByDesignerIdAndDateWithoutCancelled(designerId, date);

        // 오전 10 ~ 오후 8시까지 예약 가능하다고 가정
        LocalTime endTime = LocalTime.of(20, 0);
        LocalTime now = LocalTime.now();
        LocalTime startTime = getNextAvailableTime(now, date);

        List<TimeSlot> timeSlots = createTimeSlots(startTime, endTime, reservationList);

        return DesignerTimesResponse.builder()
                .designerId(designerId)
                .times(timeSlots)
                .build();
    }

    private List<TimeSlot> createTimeSlots(LocalTime startTime, LocalTime endTime, List<Reservation> reservationList) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        List<LocalTime> reservationTimes = reservationList.stream()
                .map(reservation -> reservation.getDate().toLocalTime()).toList();
        LocalTime currentTime = startTime;

        while (!currentTime.isAfter(endTime)) {
            final LocalTime timeSlot = currentTime;

            Boolean isAvailable = reservationTimes.stream().noneMatch(v -> v.equals(timeSlot));
            timeSlots.add(TimeSlot.builder()
                    .time(currentTime)
                    .isAvailable(isAvailable)
                    .build());

            currentTime = currentTime.plusMinutes(30);
        }

        return timeSlots;
    }

    private LocalTime getNextAvailableTime(LocalTime now, LocalDate date) {
        if(LocalDateTime.now().toLocalDate().isBefore(date)){
            return LocalTime.of(10, 0);
        }

        if (now.isBefore(LocalTime.of(10, 0))) {
            return LocalTime.of(10, 0);
        }

        int minutes = now.getMinute();
        int nextHalfHour = (minutes / 30 + 1) * 30;

        if (nextHalfHour == 60) {
            return now.plusHours(1).withMinute(0).withSecond(0).withNano(0);
        } else {
            return now.withMinute(nextHalfHour).withSecond(0).withNano(0);
        }
    }
}
