package com.keepgoing.order.presentation.dto.response;

public record ErrorCode (
    String code,
    String message
){
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
