package com.api.back.global.common;

import com.api.back.global.common.response.SuccessType;
import com.api.back.global.common.response.WrapResponse;
import com.api.back.global.config.security.jwt.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Tag(name = "Open API (no auth)")
public class CommonController {

    private final JWTUtil jwtUtil;

    public CommonController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/health")
    @Operation(summary = "Health Check (0)", description = "Health Check API")
    @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json", schema = @Schema(type = "boolean"))})
    public boolean healthCheck() {
        return true;
    }

    @GetMapping("/dev/token")
    public ResponseEntity<WrapResponse<String>> getDevToken(@RequestParam("name") String name) {

        String response = jwtUtil.createJwt("access", name, "ROLE_USER", 1000 * 60 * 60L);

        return ResponseEntity.ok(WrapResponse.create(response, SuccessType.STATUS_201));
    }
}
