package com.keepgoing.product.application.exception;

public class VendorClientException extends ClientException {

    private final String message;

    public VendorClientException(ErrorCode errorCode) {
        super(errorCode);
        message = errorCode.getMessage();
    }

    public VendorClientException(ErrorCode errorCode, Throwable cause) {
        super(errorCode);
        this.message = cause.getMessage();
    }

}
