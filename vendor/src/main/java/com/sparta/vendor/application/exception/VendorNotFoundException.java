package com.keepgoing.vendor.application.exception;

public class VendorNotFoundException extends BusinessException {

    public VendorNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
