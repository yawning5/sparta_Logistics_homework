package com.sparta.practiceorder.order.event;


import com.sparta.practiceorder.order.domain.Order;
import com.sparta.practiceorder.common.BaseEvent;
import java.util.UUID;
import lombok.Getter;

@Getter
public class OrderCreatedEvent extends BaseEvent {
    private final UUID orderId;
    private final UUID productId;
    private final int quantity;

    private OrderCreatedEvent(UUID orderId, UUID productId, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static OrderCreatedEvent of(Order order) {
        return new OrderCreatedEvent(
            order.getId(),
            order.getProductId(),
            order.getQuantity()
        );
    }
}
