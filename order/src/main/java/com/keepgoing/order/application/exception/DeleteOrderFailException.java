package com.keepgoing.order.application.exception;

import com.keepgoing.order.presentation.dto.response.base.ErrorCode;

public class DeleteOrderFailException extends BusinessException{

    public DeleteOrderFailException(String message) {
        super(ErrorCode.ORDER_DELETION_NOT_ALLOWED, message);
    }
}
