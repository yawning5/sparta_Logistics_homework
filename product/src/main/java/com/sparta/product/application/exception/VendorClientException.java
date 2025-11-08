package com.sparta.product.application.exception;

public class VendorClientException extends ClientException {

    public VendorClientException(ErrorCode errorCode) {
        super(errorCode);
    }
}
