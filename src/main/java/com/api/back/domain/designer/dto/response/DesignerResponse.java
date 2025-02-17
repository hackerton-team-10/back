package com.api.back.domain.designer.dto.response;

import com.api.back.domain.designer.type.Region;
import com.api.back.domain.designer.type.Specialty;
import com.api.back.domain.reservation.type.ConsultationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DesignerResponse {
    private Long id;
    private String name;
    private String profile;
    private String description;
    @Enumerated(EnumType.STRING)
    private ConsultationType consultationType;

    @Enumerated(EnumType.STRING)
    private Region region;

    @Enumerated(EnumType.STRING)
    private Specialty specialty;

    private Integer consultingFeeVideo;
    private Integer consultingFeeInPerson;
    private String location;
}
