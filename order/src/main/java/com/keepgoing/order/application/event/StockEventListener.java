package com.keepgoing.order.application.event;

import com.keepgoing.order.application.service.product.StockService;
import com.keepgoing.order.domain.order.event.OrderCreatedEvent;
import com.keepgoing.order.domain.payment.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockEventListener {

    private final StockService stockService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCompletedAsync(PaymentCompletedEvent event) {

        log.info("비동기 재고 차감 시작- Thread: {}, 주문 ID: {}",
            Thread.currentThread().getName(),
            event.getOrderId());

        stockService.decreaseStock(event.getProductId(), event.getQuantity());
        log.info("비동기 재고 차감 완료- 주문 ID: {}", event.getOrderId());
    }

}
