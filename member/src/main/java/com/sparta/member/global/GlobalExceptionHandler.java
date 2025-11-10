package com.keepgoing.member.global;

<<<<<<< HEAD
import com.fasterxml.jackson.databind.ser.Serializers.Base;
import com.keepgoing.member.application.dto.BaseResponseDto;
=======
import com.sparta.member.interfaces.dto.BaseResponseDto;
>>>>>>> 84b265aace245f24c5ee8f8823dd3a33829a6688
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseResponseDto<?>> handleCustomException(CustomException e) {
        logException(e);
        ErrorCode code = e.getErrorCode();

        return ResponseEntity
            .status(code.getStatus())
            .body(BaseResponseDto.error(code));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponseDto<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logException(e);

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(BaseResponseDto.error(ErrorCode.INVALID_INPUT));
    }

    /** 공통 로깅 유틸리티 */
    private static void logException(Exception e) {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace.length < 2) {
            log.error("Exception: {}", e.getMessage());
            return;
        }

        StackTraceElement origin = trace[0];
        StackTraceElement caller = trace[1];

        log.error("Exception in {}.{} (called by {}.{}): {}",
            origin.getClassName(), origin.getMethodName(),
            caller.getClassName(), caller.getMethodName(),
            e.getMessage()
        );
    }

}

