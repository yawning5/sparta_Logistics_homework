package com.keepgoing.order.domain.order.event;

import com.keepgoing.order.domain.commons.DomainEvent;
import com.keepgoing.order.domain.order.Order;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 1. 이벤트 클래스 정의
// 이벤트 클래스는 "무슨 일이 일어났는가"를 표현하는 불변 객체입니다.
// 과거형 네이밍(OrderCreated)을 사용하여 이미 발생한 사실임을 명확히 합니다.
@Getter
@AllArgsConstructor
public class OrderCreatedEvent implements DomainEvent {
    private final UUID orderId;
    private final UUID productId;
    private final int quantity;
    private final LocalDateTime occurredAt; // 이벤트 발생 시점을 기록

    public static OrderCreatedEvent of(Order order) {
        return new OrderCreatedEvent(
            order.getId(),
            order.getProductId(),
            order.getQuantity(),
            LocalDateTime.now()
        );
    }

    @Override
    public UUID getAggregateId() {
        return orderId;
    }
}
