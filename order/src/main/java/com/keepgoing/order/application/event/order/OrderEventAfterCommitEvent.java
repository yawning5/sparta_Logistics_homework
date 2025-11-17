package com.keepgoing.order.application.event.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Slf4j
@Component
public class OrderEventAfterCommitEvent {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void orderCreatedEventAfterCommit(OrderCreatedEvent event) {
        String threadName = Thread.currentThread().getName();
        log.info("AFTER COMMIT Event log - Thread: {}, orderId: {}, productId: {}, quantity: {}, totalPrice: {}, time: {}", threadName, event.orderId(), event.productId(), event.quantity(), event.totalPrice(), LocalDateTime.now());
    }
}
