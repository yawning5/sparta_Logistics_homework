package com.keepgoing.order.application.exception;

import com.keepgoing.order.presentation.dto.response.base.ErrorCode;

public class NotFoundProductException extends BusinessException{

    public NotFoundProductException(String message) {
        super(ErrorCode.PRODUCT_NOT_FOUND ,message);
    }
}
