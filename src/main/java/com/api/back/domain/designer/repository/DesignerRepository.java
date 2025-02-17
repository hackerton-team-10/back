package com.api.back.domain.designer.repository;

import com.api.back.domain.designer.entity.Designer;
import com.api.back.domain.designer.type.Region;
import com.api.back.domain.reservation.type.ConsultationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DesignerRepository extends JpaRepository<Designer, Long> {
    @Query("SELECT d FROM Designer d " +
            "WHERE (:region IS NULL OR d.region = :region) " +
            "AND (:consultationType IS NULL OR d.consultationType = :consultationType) " +
            "AND ((" +
            "    (:consultationType = 'in_person' AND d.consultingFeeInPerson BETWEEN COALESCE(:minFee, 0) AND COALESCE(:maxFee, 2147483647))" +
            ") OR (" +
            "    (:consultationType = 'video' AND d.consultingFeeVideo BETWEEN COALESCE(:minFee, 0) AND COALESCE(:maxFee, 2147483647))" +
            ") OR (" +
            "    (:consultationType IS NULL AND (d.consultingFeeVideo BETWEEN COALESCE(:minFee, 0) AND COALESCE(:maxFee, 2147483647) " +
            "    OR d.consultingFeeInPerson BETWEEN COALESCE(:minFee, 0) AND COALESCE(:maxFee, 2147483647)))" +
            "))")
    List<Designer> findAllByFilters(
            @Param("region") Region region,
            @Param("consultationType") ConsultationType consultationType,
            @Param("minFee") Integer minFee,
            @Param("maxFee") Integer maxFee
    );
}
