package com.api.back.domain.designer.dto.response;

import com.api.back.domain.designer.type.Specialty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DesignerInfo {
    Long designerId;
    String designerName;
    Specialty specialty;
    String location;
}
