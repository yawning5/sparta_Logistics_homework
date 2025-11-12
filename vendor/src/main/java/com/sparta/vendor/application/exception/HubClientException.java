package com.sparta.vendor.application.exception;

public class HubClientException extends ClientException {

    private final String message;

    public HubClientException(ErrorCode errorCode) {
        super(errorCode);
        message = errorCode.getMessage();
    }

    public HubClientException(ErrorCode errorCode, Throwable cause) {
        super(errorCode);
        this.message = cause.getMessage();
    }
}
