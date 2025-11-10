package com.keepgoing.order.application.exception;

public class InventoryReservationFailException extends RuntimeException{

    public InventoryReservationFailException(String message) {
        super(message);
    }
}
