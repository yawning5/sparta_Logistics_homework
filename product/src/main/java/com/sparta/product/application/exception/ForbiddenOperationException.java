package com.keepgoing.product.application.exception;

public class ForbiddenOperationException extends BusinessException{

    public ForbiddenOperationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
