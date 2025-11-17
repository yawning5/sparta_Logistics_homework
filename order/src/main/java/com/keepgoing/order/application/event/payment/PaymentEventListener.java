package com.keepgoing.order.application.event.payment;

import com.keepgoing.order.application.event.order.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final PaymentService paymentService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void paymentCreatedEventAfterCommit(OrderCreatedEvent event) {
        String threadName = Thread.currentThread().getName();
        log.info("결제 시도 - Thread: {}, orderId: {}", threadName, event.orderId());

        paymentService.processPayment(event.orderId(), event.productId(), event.quantity(), event.totalPrice());

        log.info("결제 성공 → PaymentCompletedEvent 발행 완료");
    }
}
