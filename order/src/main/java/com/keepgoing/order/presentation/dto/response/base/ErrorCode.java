package com.keepgoing.order.presentation.dto.response.base;

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
