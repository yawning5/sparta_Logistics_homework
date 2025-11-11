package com.keepgoing.order.application.exception;

import com.keepgoing.order.presentation.dto.response.base.ErrorCode;

public class NotFoundOrderException extends BusinessException{

    public NotFoundOrderException(String message) {
        super(ErrorCode.ORDER_NOT_FOUND, message);
    }
}
