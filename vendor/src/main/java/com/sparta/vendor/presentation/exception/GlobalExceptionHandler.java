package com.sparta.vendor.presentation.exception;

import com.sparta.vendor.application.exception.BusinessException;
import com.sparta.vendor.application.exception.ErrorCode;
import com.sparta.vendor.presentation.dto.BaseResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponseDTO<?>> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getHttpStatus()) // ErrorCode에 HttpStatus 있으면 사용
            .body(BaseResponseDTO.error(errorCode));
    }
}
