package com.keepgoing.order.application.event.stock;

import com.keepgoing.order.application.event.payment.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockEventListener {

    private final StockService stockService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void decreaseStock(PaymentCompletedEvent event) {
        log.info("결제 완료 이벤트 수신");

        Integer stock = stockService.decreaseStock(event.productId(), event.quantity());

        log.info("재고 차감 완료 - 상품 ID: {}, 차감 후 수량: {}", event.productId(), stock);
    }
}
