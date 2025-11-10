package com.keepgoing.order.domain.order;

public enum CancelState {

    NONE("주문"),
    CANCEL_REQUESTED("주문 취소 요청"),

    ORDER_CANCEL_AWAITING("주문 취소 대기"),
    ORDER_CANCEL_IN_PROGRESS("주문 취소 진행중"),
    ORDER_CANCELLED("주문 취소 완료"),

    INVENTORY_RESERVATION_CANCEL_AWAITING("재고 예약 취소 대기"),
    INVENTORY_RESERVATION_CANCEL_IN_PROGRESS("재고 예약 취소 진행중"),

    PAYMENT_CANCEL_AWAITING("결제 취소 대기"),
    PAYMENT_CANCEL_IN_PROGRESS("결제 취소 진행중"),
    ;

    private String descirption;

    CancelState(String descirption) {
        this.descirption = descirption;
    }
}
