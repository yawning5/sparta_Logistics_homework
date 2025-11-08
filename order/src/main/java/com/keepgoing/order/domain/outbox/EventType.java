package com.keepgoing.order.domain.outbox;

public enum EventType {

    COMPLETED("주문 완료"),
    PAID("결제 완료")
    ;

    private String description;

    EventType(String description){
        this.description = description;
    }
}
