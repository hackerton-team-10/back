package com.api.back.domain.designer.api;

import com.api.back.domain.designer.application.DesignerService;
import com.api.back.domain.designer.dto.response.DesignerResponse;
import com.api.back.domain.designer.dto.response.DesignerTimesResponse;
import com.api.back.domain.designer.type.Region;
import com.api.back.domain.designer.type.Specialty;
import com.api.back.domain.reservation.type.ConsultationType;
import com.api.back.global.common.response.SuccessType;
import com.api.back.global.common.response.WrapResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/designer")
public class DesignerApi implements DesignerApiDocs{
    private final DesignerService designerService;

    @GetMapping("")
    public ResponseEntity<WrapResponse<List<DesignerResponse>>> designerList(
            @RequestParam(value="region", required = false) Optional<Region> region,
            @RequestParam(value="consultationType", required = false) Optional<ConsultationType> consultationType,
            @RequestParam(value="specialty", required = false) Optional<Specialty> specialty,
            @RequestParam(value="minFee", required = false) Optional<Integer> minFee,
            @RequestParam(value="maxFee", required = false) Optional<Integer> maxFee
    ){
        List<DesignerResponse> response = designerService.getDesignerList(region, consultationType, specialty, minFee, maxFee);
        return ResponseEntity.ok(WrapResponse.create(response, SuccessType.SIMPLE_STATUS));
    }

    @GetMapping("/{designerId}")
    public ResponseEntity<WrapResponse<DesignerResponse>> designer(
            @PathVariable(value="designerId", required = true) Long id
    ){
        DesignerResponse response = designerService.getDesigner(id);
        return ResponseEntity.ok(WrapResponse.create(response, SuccessType.SIMPLE_STATUS));
    }

    @Override
    @GetMapping("reservation")
    public ResponseEntity<WrapResponse<DesignerTimesResponse>> designerReservation(Long designerId, LocalDate date) {
        DesignerTimesResponse response = designerService.getDesignerTimes(designerId, date);
        return ResponseEntity.ok(WrapResponse.create(response, SuccessType.SIMPLE_STATUS));
    }
}
