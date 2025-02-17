package com.api.back.domain.designer.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DesignerTimesResponse {
    Long designerId;
    List<TimeSlot> times;
}
