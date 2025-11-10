package com.keepgoing.vendor.presentation.exception;

import com.keepgoing.vendor.application.exception.BusinessException;
import com.keepgoing.vendor.application.exception.ErrorCode;
import com.keepgoing.vendor.presentation.dto.BaseResponseDTO;
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
