package com.keepgoing.order.application.exception;

public class StateUpdateFailedException extends RuntimeException{

    public StateUpdateFailedException(String message) {
        super(message);
    }
}
