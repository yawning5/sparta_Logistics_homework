package com.keepgoing.order.domain.outbox;

public enum AggregateType {

    ORDER("주문")
    ;

    private String description;

    AggregateType(String description){
        this.description = description;
    }
}
