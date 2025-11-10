package com.keepgoing.delivery.global;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    HUB_DELIVERY_PERSON_NOT_FOUND(HttpStatus.NO_CONTENT, "7001", "허브 배송 담당자가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
