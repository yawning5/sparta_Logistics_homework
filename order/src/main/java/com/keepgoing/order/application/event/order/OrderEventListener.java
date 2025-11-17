package com.keepgoing.order.application.event.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class OrderEventListener {

    @EventListener
    public void orderCreatedEvent(OrderCreatedEvent event) {
        String threadName = Thread.currentThread().getName();
        log.info("Event log - Thread: {}, orderId: {}, productId: {}, quantity: {}, totalPrice: {}, time: {}", threadName, event.orderId(), event.productId(), event.quantity(), event.totalPrice(),  LocalDateTime.now());
    }


}
