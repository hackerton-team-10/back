package com.api.back.domain.designer.api;

import com.api.back.domain.designer.dto.response.DesignerResponse;
import com.api.back.domain.designer.dto.response.DesignerTimesResponse;
import com.api.back.domain.designer.type.Region;
import com.api.back.domain.designer.type.Specialty;
import com.api.back.domain.reservation.type.ConsultationType;
import com.api.back.global.common.response.WrapResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Tag(name = "디자이너 API", description = "디자이너 API")
public interface DesignerApiDocs {
    @Operation(summary = "디자이너 리스트 조회")
    @GetMapping({""})
    public ResponseEntity<WrapResponse<List<DesignerResponse>>> designerList(
            @RequestParam(value = "region", required = false) Optional<Region> region,
            @RequestParam(value = "consultationType", required = false) Optional<ConsultationType> consultationType,
            @RequestParam(value="specialty", required = false) Optional<Specialty> specialty,
            @RequestParam(value = "minFee", required = false) Optional<Integer> minFee,
            @RequestParam(value = "maxFee", required = false) Optional<Integer> maxFee
    );

    @Operation(summary = "디자이너 조회")
    @GetMapping("/{designerId}")
    public ResponseEntity<WrapResponse<DesignerResponse>> designer(
            @Parameter(description = "디자이너 ID", example = "1")
            @PathVariable(value = "designerId", required = true) Long designerId
    );

    @Operation(summary = "디자이너 예약 시간 조회")
    @GetMapping("reservation")
    public ResponseEntity<WrapResponse<DesignerTimesResponse>> designerReservation(
            @Parameter(description = "디자이너 ID", example = "1")
            @RequestParam(value = "designerId", required = true)
            Long designerId,

            @Parameter(description = "예약 날짜", example = "2025-02-17")
            @RequestParam(value = "date", required = true)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    );
}
