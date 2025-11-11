package com.keepgoing.order.application.exception;

import com.keepgoing.order.presentation.dto.response.base.ErrorCode;

public class InventoryReservationFailException extends BusinessException{

    public InventoryReservationFailException(String message) {
        super(ErrorCode.INVENTORY_RESERVATION_FAIL, message);
    }
}
