package com.api.back.domain.designer.application;

import com.api.back.domain.designer.dto.response.DesignerResponse;
import com.api.back.domain.designer.dto.response.DesignerTimesResponse;
import com.api.back.domain.designer.type.Region;
import com.api.back.domain.designer.type.Specialty;
import com.api.back.domain.reservation.type.ConsultationType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DesignerService {
    List<DesignerResponse> getDesignerList(Optional<Region> region, Optional<ConsultationType> consultationType, Optional<Specialty> specialty, Optional<Integer> minFee, Optional<Integer> maxFee);
    DesignerResponse getDesigner(Long designerId);
    DesignerTimesResponse getDesignerTimes(Long designerId, LocalDate date);
}
