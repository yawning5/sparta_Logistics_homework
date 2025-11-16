package com.keepgoing.order.domain.event;

import com.keepgoing.order.domain.model.Order;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderCreatedEvent {
    private final UUID orderId;
    private final UUID productId;
    private final LocalDateTime occurredAt;

    public static OrderCreatedEvent of(Order order, LocalDateTime now) {
        return new OrderCreatedEvent(
            order.getId(),
            order.getProduct().productId(),
            now
        );
    }
}
