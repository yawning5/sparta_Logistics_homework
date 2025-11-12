package com.keepgoing.order.application.exception;

import com.keepgoing.order.presentation.dto.response.base.ErrorCode;

public class OrderCancelFailException extends BusinessException{

    public OrderCancelFailException(String message) {
        super(ErrorCode.ORDER_CANCEL_FAIL ,message);
    }
}
