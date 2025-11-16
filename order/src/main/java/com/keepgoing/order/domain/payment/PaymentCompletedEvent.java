package com.keepgoing.order.domain.payment;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentCompletedEvent {

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

}
