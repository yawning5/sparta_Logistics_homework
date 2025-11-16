package com.keepgoing.order.domain.payment.event;

import com.keepgoing.order.domain.commons.DomainEvent;
import com.keepgoing.order.domain.payment.Payment;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentCompletedEvent implements DomainEvent {

    private final UUID orderId;
    private final UUID productId;
    private final int quantity;

    public static PaymentCompletedEvent of(Payment payment, UUID productId, int quantity) {
        return new PaymentCompletedEvent(
            payment.getOrderId(),
            productId,
            quantity
        );
    }

    @Override
    public UUID getAggregateId() {
        return orderId;
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return LocalDateTime.now();
    }
}
