package com.keepgoing.order.application.event.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class OrderAsyncCreateEvent {

    public final List<String> eventThreads = new ArrayList<>();

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void orderAsyncCreatedEventAfterCommit(OrderCreatedEvent event) {
        String threadName = Thread.currentThread().getName();

        eventThreads.add(threadName);

        log.info("Async Event log - Thread: {}, orderId: {}, productId: {}, quantity: {}, totalPrice: {}, time: {}", threadName, event.orderId(), event.productId(), event.quantity(), event.totalPrice(), LocalDateTime.now());
    }
}
