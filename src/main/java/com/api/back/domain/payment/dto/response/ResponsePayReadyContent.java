package com.api.back.domain.payment.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ResponsePayReadyContent {
    String tid;
    boolean tms_result;
    String next_redirect_app_url;
    String next_redirect_mobile_url;
    String next_redirect_pc_url;
    String android_app_scheme;
    String ios_app_scheme;
    LocalDateTime created_at;
}
