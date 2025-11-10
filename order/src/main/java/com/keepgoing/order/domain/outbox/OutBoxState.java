package com.keepgoing.order.domain.outbox;

public enum OutBoxState {

    DELIVERY_PENDING("배송 이벤트 발급 대기"),
    DELIVERY_COMPLETED("배송 이벤트 발급 완료"),
    DELIVERY_FAILED("배송 이벤트 발급 실패"),
    DELIVERY_COMPLETION_IN_PROGRESS("배송 이벤트 발급중"),

    NOTIFICATION_PENDING("알림 이벤트 발급 대기"),
    NOTIFICATION_COMPLETED("알림 이벤트 발급 완료"),
    NOTIFICATION_FAILED("알림 이벤트 발급 실패"),
    NOTIFICATION_COMPLETION_IN_PROGRESS("알림 이벤트 발급중")
    ;

    private String description;

    OutBoxState(String description) {
        this.description = description;
    }
}
