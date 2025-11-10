package com.keepgoing.hub.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, 6204, "허브를 찾을 수 없습니다."),
    HUB_ALREADY_DELETED(HttpStatus.BAD_REQUEST, 6400, "이미 삭제된 허브입니다.");

    private final HttpStatus status;
    private final int code;
    private final String message;

    ErrorCode(HttpStatus status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}