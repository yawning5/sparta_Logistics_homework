package com.sparta.product.application.exception;

public class ProductNotFoundException extends BusinessException {

    public ProductNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
