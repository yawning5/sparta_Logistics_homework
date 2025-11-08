package com.sparta.product.presentation.exception;

import com.sparta.product.application.exception.BusinessException;
import com.sparta.product.application.exception.ClientException;
import com.sparta.product.application.exception.ErrorCode;
import com.sparta.product.presentation.dto.BaseResponseDTO;
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

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<BaseResponseDTO<?>> handleClientException(ClientException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getHttpStatus()) // ErrorCode에 HttpStatus 있으면 사용
            .body(BaseResponseDTO.error(errorCode));
    }
}
