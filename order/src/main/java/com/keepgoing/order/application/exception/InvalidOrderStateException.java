package com.keepgoing.order.application.exception;

import com.keepgoing.order.presentation.dto.response.base.ErrorCode;

public class InvalidOrderStateException extends BusinessException{

    public InvalidOrderStateException(String message) {
        super(ErrorCode.ORDER_INVALID_STATE, message);
    }
}
