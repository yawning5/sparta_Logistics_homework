package com.keepgoing.order.application.exception;

import com.keepgoing.order.presentation.dto.response.base.ErrorCode;

public class StateUpdateFailedException extends BusinessException{

    public StateUpdateFailedException(String message) {
        super(ErrorCode.ORDER_STATE_UPDATE_NOT_ALLOWED ,message);
    }
}
