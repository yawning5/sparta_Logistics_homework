package com.keepgoing.order.application.exception;

public class DeleteOrderFailException extends RuntimeException{

    public DeleteOrderFailException(String message) {
        super(message);
    }
}
