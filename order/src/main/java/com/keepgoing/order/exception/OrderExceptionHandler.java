package com.keepgoing.order.exception;

import com.keepgoing.order.application.exception.BusinessException;
import com.keepgoing.order.presentation.dto.response.base.BaseResponseDto;
import com.keepgoing.order.presentation.dto.response.base.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "OrderExceptionHandler")
@RestControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponseDto<?>> handleBusinessException(BusinessException ex) {
        ErrorCode ec = ex.getErrorCode();

        log.error("BusinessException {} - {}", ec.getCode(), ex.getMessage());

        return ResponseEntity
            .status(ec.getHttpStatus())
            .body(BaseResponseDto.error(ec.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponseDto<?>> handleValidation(MethodArgumentNotValidException ex) {

        log.error("유효성 검증 실패 : {}", ex.getMessage());

        return ResponseEntity
            .status(ErrorCode.INVALID_JSON_FORMAT.getHttpStatus())
            .body(BaseResponseDto.error(
                ErrorCode.INVALID_JSON_FORMAT.getCode(),
                ex.getBindingResult()
                    .getFieldErrors()
                    .get(0)
                    .getDefaultMessage()
                )
            );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponseDto<?>> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
            .findFirst()
            .map(ConstraintViolation::getMessage) // 애너테이션에 적은 message
            .orElse("요청 값이 유효하지 않습니다.");

        log.error("유효성 검증 실패 : {} - {}", message ,ex.getMessage());

        return ResponseEntity
            .status(ErrorCode.INVALID_JSON_FORMAT.getHttpStatus())
            .body(BaseResponseDto.error(
                ErrorCode.INVALID_JSON_FORMAT.getCode(),
                message
            ));
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<BaseResponseDto<?>> handleNoToken(AuthenticationCredentialsNotFoundException e) {

        log.warn("토큰 없음 : {}", e.getMessage());

        return ResponseEntity
            .status(ErrorCode.UNAUTHORIZED_NO_TOKEN.getHttpStatus())
            .body(BaseResponseDto.error(ErrorCode.UNAUTHORIZED_NO_TOKEN));
    }

    @ExceptionHandler({ExpiredJwtException.class, CredentialsExpiredException.class})
    public ResponseEntity<BaseResponseDto<?>> handleExpiredJwt(Exception e) {

        log.warn("토큰 만료: {}", e.getMessage());

        return ResponseEntity
            .status(ErrorCode.UNAUTHORIZED_TOKEN_EXPIRED.getHttpStatus())
            .body(BaseResponseDto.error(ErrorCode.UNAUTHORIZED_TOKEN_EXPIRED));
    }

    @ExceptionHandler({JwtException.class, BadCredentialsException.class})
    public ResponseEntity<BaseResponseDto<?>> handleInvalidJwt(RuntimeException e) {

        log.warn("유효하지 않은 토큰 : {}", e.getMessage());

        return ResponseEntity
            .status(ErrorCode.UNAUTHORIZED_INVALID_TOKEN.getHttpStatus())
            .body(BaseResponseDto.error(ErrorCode.UNAUTHORIZED_INVALID_TOKEN));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponseDto<?>> handleAccessDenied(AccessDeniedException e) {

        log.warn("인가 실패 : {}", e.getMessage());

        return ResponseEntity
            .status(ErrorCode.FORBIDDEN_ACCESS.getHttpStatus())
            .body(BaseResponseDto.error(ErrorCode.FORBIDDEN_ACCESS));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseDto<?>> handleException(Exception ex) {
        log.error("서버 오류 발생", ex);

        ErrorCode ec = ErrorCode.INTERNAL_SERVER_ERROR;

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(BaseResponseDto.error(ec));
    }
}
