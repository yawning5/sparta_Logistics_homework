package com.keepgoing.order.application.exception;

public class NotFoundOrderException extends RuntimeException{

    public NotFoundOrderException(String message) {
        super(message);
    }
}
