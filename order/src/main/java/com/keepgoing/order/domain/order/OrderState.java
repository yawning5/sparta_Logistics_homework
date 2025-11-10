package com.keepgoing.order.domain.order;

public enum OrderState {

    PENDING_VALIDATION("상품 검증 대기"),
    PRODUCT_VALIDATION_IN_PROGRESS("상품 확인 진행중"),
    PRODUCT_VERIFIED("상품 검증 완료"),

    STOCK_RESERVATION_IN_PROGRESS("재고 예약 진행중"),
    AWAITING_PAYMENT("결제 대기"),

    PAYMENT_IN_PROGRESS("결제 진행중"),
    PAID("결제 완료"),

    COMPLETION_IN_PROGRESS("주문 진행 중"),
    COMPLETED("주문 완료"),

    ORDER_CONFIRMED("주문 확정"),

    CANCEL_REQUESTED("주문 취소 요청"),
    PAYMENT_CANCELLED("결제 취소 완료"),
    STOCK_RELEASED("재고 복구 완료"),
    CANCELED("주문 취소 완료"),
    FAILED("주문 실패")
    ;
    
    private String description;

    OrderState(String description) {
        this.description = description;
    }

}
