package com.api.back.domain.reservation.repository;

import com.api.back.domain.reservation.entity.Reservation;
import com.api.back.domain.reservation.type.ReservationStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @EntityGraph(attributePaths = {"designer", "payment"})
    List<Reservation> findAllByMemberIdAndStatus(Long memberId, ReservationStatus status);
    List<Reservation> findAllByMemberId(Long memberId);
    @Query("SELECT r FROM Reservation r WHERE r.designer.id = :designerId AND FUNCTION('DATE', r.date) = :date AND r.status <> 'CANCELLED'")
    List<Reservation> findAllByDesignerIdAndDateWithoutCancelled(@Param("designerId") Long designerId, @Param("date") LocalDate date);

    List<Reservation> findAllByDesignerIdAndDate(Long designerId, LocalDateTime date);
}
