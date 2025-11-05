package com.keepgoing.order.domain;

public enum OrderState {

    PENDING_VALIDATION("상품 검증 대기"),
    PRODUCT_VERIFIED("상품 확인"),
    STOCK_RESERVED("재고 예약"),
    AWAITING_PAYMENT("결제 대기"),
    PAID("결제 완료"),
    CONFIRMED("주문 완료"),

    CANCEL_REQUESTED("주문 취소 요청"),
    PAYMENT_CANCELLED("결제 취소 완료"),
    STOCK_RELEASED("재고 복구 완료"),
    CANCELED("주문 취소 완료")
    ;
    
    private String description;

    OrderState(String description) {
        this.description = description;
    }

}
