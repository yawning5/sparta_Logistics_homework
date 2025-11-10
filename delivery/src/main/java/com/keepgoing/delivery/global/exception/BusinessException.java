package com.keepgoing.delivery.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 예외 메시지를 상위 RuntimeException에 전달
        this.errorCode = errorCode;
    }
}