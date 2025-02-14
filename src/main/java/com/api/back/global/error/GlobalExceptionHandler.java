package com.api.back.global.error;

import com.api.back.global.error.exception.BusinessLogicException;
import com.api.back.global.error.exception.ErrorCode;
import com.api.back.global.error.exception.InvalidValueException;
import com.api.back.global.error.exception.SimpleException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 서버 내부에서 잘못된 비즈니스 로직으로 인해 발생하는 예외처리 핸들러 (500번 에러로 처리)
     * **/
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> handleBusinessLogicException(final BusinessLogicException e) {
        final ErrorResponse errorResponse = new ErrorResponse(e.getStatus(), e.getMessage());
        log.error(e.getMessage(), e);

        return ResponseEntity.internalServerError()
            .body(errorResponse);
    }

    /**
     * 잘못된 값에 의한 예외처리 핸들러 (400번 에러로 처리)
     * **/
    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<ErrorResponse> handleInvalidValueException(final InvalidValueException e) {
        final ErrorResponse errorResponse = new ErrorResponse(e.getStatus(), e.getMessage());
        log.error(e.getMessage(), e);

        return ResponseEntity.badRequest()
            .body(errorResponse);
    }

    /**
     * 그 밖의 예외사항에 대한 예외처리 핸들러
     * **/
    @ExceptionHandler(SimpleException.class)
    public ResponseEntity<ErrorResponse> handleSimpleException(final SimpleException e){
        final ErrorResponse errorResponse = new ErrorResponse(e.getStatus(), e.getMessage());
        log.error(e.getMessage(), e);

        return ResponseEntity.internalServerError()
            .body(errorResponse);
    }


    /**
     * MissingServletRequestParameterException handler (400번 에러로 처리)
     * 쿼리 파라미터가 누락되었을 때 발생하는 Exception
     * **/
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(final MissingServletRequestParameterException e){
        ErrorCode errorType = ErrorCode.INPUT_VALUE_INVALID;
        StringBuffer sb = new StringBuffer();
        sb.append(e.getParameterName()).append("가 비었습니다.");
        if(sb.isEmpty()){
            sb.append(errorType.getMessage());
        }
        final ErrorResponse errorResponse = new ErrorResponse(errorType.getStatus(), sb.toString());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /*
     * ConstraintViolationException handler (400번 에러로 처리)
     * jakarta.validation 의 어노테이션에서 발생하는 Exception
     * */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(final ConstraintViolationException e){
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String [] errors = violations.stream()
            .map(ConstraintViolation::getMessage)
            .toArray(String[]::new);
        ErrorCode type = ErrorCode.INPUT_VALUE_INVALID;
        String clientMessage = type.getMessage();
        if(errors.length > 0) {
            clientMessage = String.join(", ", errors);
        }
        final ErrorResponse errorResponse = new ErrorResponse(type.getStatus(), clientMessage);
        log.error(e.getMessage(), e);
        return ResponseEntity.badRequest()
            .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        ErrorCode errorType = ErrorCode.ARGUMENT_TYPE_MISMATCH;
        final ErrorResponse errorResponse = new ErrorResponse(errorType.getStatus(), errorType.getMessage());
        log.error(e.getMessage(), e);

        return ResponseEntity.badRequest()
            .body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(final IllegalStateException e) {
        ErrorCode errorType = ErrorCode.ARGUMENT_TYPE_MISMATCH;
        final ErrorResponse errorResponse = new ErrorResponse(errorType.getStatus(), errorType.getMessage());
        log.error(e.getMessage(), e);

        return ResponseEntity.badRequest()
            .body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> IllegalArgumentException(final IllegalArgumentException e) {
        ErrorCode errorType = ErrorCode.ARGUMENT_TYPE_MISMATCH;
        final ErrorResponse errorResponse = new ErrorResponse(errorType.getStatus(), errorType.getMessage());
        log.error(e.getMessage(), e);

        return ResponseEntity.badRequest()
            .body(errorResponse);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> SQLIntegrityConstraintViolationException(final IllegalArgumentException e) {
        ErrorCode errorType = ErrorCode.QUERY_INVALID_PARAM;
        final ErrorResponse errorResponse = new ErrorResponse(errorType.getStatus(), errorType.getMessage());
        log.error(e.getMessage(), e);

        return ResponseEntity.badRequest()
            .body(errorResponse);
    }

}