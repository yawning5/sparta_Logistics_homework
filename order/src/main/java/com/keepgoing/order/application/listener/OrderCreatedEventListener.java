package com.keepgoing.order.application.listener;

import com.keepgoing.order.application.service.OrderService;
import com.keepgoing.order.domain.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j(topic = "OrderCreatedEvent")
@Component
@RequiredArgsConstructor
public class OrderCreatedEventListener {

    private final OrderService orderService;

    @EventListener
    public void handleOrderCreadted(OrderCreatedEvent event) {
        log.info("주문 생성 이벤트 수신 - 주문 ID: {}, 상품 ID: {}, 이벤트 발생 시간 {}"
            , event.getOrderId(), event.getProductId(), event.getOccurredAt());
    }
}
