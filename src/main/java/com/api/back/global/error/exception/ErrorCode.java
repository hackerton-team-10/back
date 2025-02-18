package com.api.back.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Global
    INTERNAL_SERVER_ERROR(500, "내부 서버 오류입니다."),
    METHOD_NOT_ALLOWED(405, "허용되지 않은 HTTP method입니다."),
    INPUT_VALUE_INVALID(400, "유효하지 않은 입력입니다."),
    INPUT_TYPE_INVALID(400, "입력 타입이 유효하지 않습니다."),
    HTTP_MESSAGE_NOT_READABLE(400, "request message body가 없거나, 값 타입이 올바르지 않습니다."),
    HTTP_HEADER_INVALID(400, "request header가 유효하지 않습니다."),
    ENTITY_NOT_FOUND(404, "존재하지 않는 Entity입니다."),
    FORBIDDEN_ERROR(403, "작업을 수행하기 위한 권한이 없습니다."),
    UNSUPPORTED_MEDIA_TYPE(415, "지원하지 않는 파일 형식입니다."),
    ARGUMENT_TYPE_MISMATCH(400, "잘못된 타입의 파라미터 요청입니다."),
    INVALID_REQUEST_PARAM(400, "잘못된 요청입니다."),
    QUERY_INVALID_PARAM(400, "잘못된 요청입니다."),

    // Member
    ACCESSTOKEN_EXPIRED(401,"엑세스 토큰이 만료되었습니다 재로그인이 필요합니다."),
    REFRESHTOKEN_EXPIRED(400,"리프레시 토큰이 만료되었습니다 재로그인이 필요합니다."),
    REFRESHTOKEN_INVALID(400,"리프레시 토큰이 잘못되었습니다 재로그인이 필요합니다."),
    TOKEN_NOT_FOUND(404,"토큰을 찾을 수 없습니다."), //redis속 refresh토큰이 없을때
    TOKEN_CREATE_FAILED(432,"토큰 생성 실패했습니다."), //access 토큰재발급에 실패했을때
    MEMBER_NOT_FOUND(404, "존재하지 않는 회원입니다."),
    MEMBER_NOT_POSSESSION(404, "보유하고 있지않은 아이콘입니다."),
    MEMBER_ALREADY_ICON(400, "변경하려는 아이콘이 현재 아이콘과 동일한 아이콘입니다."),

    // Designer
    DESIGNER_NOT_FOUND(404, "존재하지 않는 디자이너입니다."),

    // Reservation
    RESERVATION_NOT_FOUND(404, "존재하지 않는 예약입니다."),
    RESERVATION_NOT_AVAILABLE(400, "현재 예약할 수 없는 상태입니다."),

    //Payment
    PAYMENT_NOT_FOUND(404, "존재하지 않는 결제입니다.")
    ;

    private final int status;
    private final String message;

}