package com.keepgoing.vendor.application.exception;

public class VendorDeletedException extends BusinessException{

    public VendorDeletedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
