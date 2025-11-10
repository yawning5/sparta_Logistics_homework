package com.sparta.product.application.exception;

public class ProductDeletedException extends BusinessException {

    public ProductDeletedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
