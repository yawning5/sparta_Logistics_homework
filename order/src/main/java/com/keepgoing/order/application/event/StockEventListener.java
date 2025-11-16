package com.keepgoing.order.application.event;

import com.keepgoing.order.application.service.product.StockService;
import com.keepgoing.order.domain.payment.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockEventListener extends AsyncTransactionalEventListener<PaymentCompletedEvent> {

    private final StockService stockService;


    @Override
    public void handleEvent(PaymentCompletedEvent event) {
        super.handleEvent(event);
    }

    @Override
    protected void processEvent(PaymentCompletedEvent event) {
        log.info("비동기 재고 차감 시작- Thread: {}, 주문 ID: {}",
            Thread.currentThread().getName(),
            event.getOrderId());

        stockService.decreaseStock(event.getProductId(), event.getQuantity());
        log.info("비동기 재고 차감 완료- 주문 ID: {}", event.getOrderId());
    }

    @Override
    protected void handleException(PaymentCompletedEvent event, Exception e) {
        super.handleException(event, e);
    }
}
