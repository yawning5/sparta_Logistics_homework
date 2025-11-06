package com.keepgoing.order.domain;

public enum EventType {

    ORDER_CREATE("주문 생성")
    ;

    private String description;

    EventType(String description){
        this.description = description;
    }
}
