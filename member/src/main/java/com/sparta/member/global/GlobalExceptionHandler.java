package com.sparta.member.global;

import com.sparta.member.application.dto.BaseResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseResponseDto<?>> handleCustomException(CustomException e) {
        ErrorCode code = e.getErrorCode();

        StackTraceElement origin = e.getStackTrace()[0];
        StackTraceElement caller = e.getStackTrace()[1];

        log.error("Exception in {}.{} (called by {}.{}):{}",
            origin.getClassName(), origin.getMethodName(),
            caller.getClassName(), caller.getMethodName(),
            e.getMessage()
        );

        return ResponseEntity
            .status(code.getStatus())
            .body(BaseResponseDto.error(code));
    }

}

