package com.api.back.global.error;

import lombok.Builder;

@Builder
public record ErrorResponse(int errorCode, String errorMessage) {

}
