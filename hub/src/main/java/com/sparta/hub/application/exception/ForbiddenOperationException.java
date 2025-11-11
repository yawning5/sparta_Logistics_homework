package com.sparta.hub.application.exception;

public class ForbiddenOperationException extends RuntimeException {
    private final ErrorCode errorCode;

    public ForbiddenOperationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}