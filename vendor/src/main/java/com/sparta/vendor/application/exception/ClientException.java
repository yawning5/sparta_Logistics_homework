package com.sparta.vendor.application.exception;

public abstract class ClientException extends RuntimeException {

    private final ErrorCode errorCode;

    protected ClientException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
