package com.keepgoing.order.application.exception;

public class NotFoundProductException extends RuntimeException{

    public NotFoundProductException(String message) {
        super(message);
    }
}
