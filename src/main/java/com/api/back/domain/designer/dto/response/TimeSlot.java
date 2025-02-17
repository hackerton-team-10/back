package com.api.back.domain.designer.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Builder
@Getter
public class TimeSlot {
    @JsonFormat(pattern = "HH:mm")
    LocalTime time;
    Boolean isAvailable;
}
