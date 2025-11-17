package com.sparta.practiceorder.payment.event;

import com.sparta.practiceorder.common.BaseEvent;
import com.sparta.practiceorder.payment.domain.Payment;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentCompletedEvent extends BaseEvent {

    private final UUID paymentId;
    private final UUID orderId;
    private final UUID productId;
    private final Integer quantity;


    public static PaymentCompletedEvent of(Payment payment, UUID orderId, UUID productId, Integer quantity) {
        return new PaymentCompletedEvent(payment.getId(), orderId, productId, quantity);
    }
}
