package com.keepgoing.order.application.event.payment;

import java.util.UUID;

public record PaymentCompletedEvent(
        UUID productId,
        Integer quantity
) {
    public static PaymentCompletedEvent from(Payment payment) {
        return new PaymentCompletedEvent(
                payment.getProductId(),
                payment.getQuantity()
        );
    }
}
