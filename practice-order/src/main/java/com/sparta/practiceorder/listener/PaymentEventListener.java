package com.sparta.practiceorder.listener;

import com.sparta.practiceorder.events.OrderCreatedEvent;
import com.sparta.practiceorder.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventListener {

    private final PaymentService paymentService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("결제 처리 시작- 주문 ID: {}", event.getOrderId());

        paymentService.processPayment(
            event.getOrderId(),
            event.getProductId(),
            event.getQuantity()
        );

        log.info("결제 처리 완료 - 주문 ID: {}", event.getOrderId());
    }

}
