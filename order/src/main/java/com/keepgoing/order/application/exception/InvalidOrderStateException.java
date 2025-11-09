package com.keepgoing.order.application.exception;

public class InvalidOrderStateException extends RuntimeException{

    public InvalidOrderStateException(String message) {
        super(message);
    }
}
