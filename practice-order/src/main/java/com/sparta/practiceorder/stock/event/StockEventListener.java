package com.sparta.practiceorder.stock.event;

import com.sparta.practiceorder.payment.event.PaymentCompletedEvent;
import com.sparta.practiceorder.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class StockEventListener {

    private final StockService stockService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handlePaymentCompletedEvent(PaymentCompletedEvent event) {
        log.info("비동기 재고 차감 처리 시작 - Thread: {}, 상품 Id: {}, 차감 수량: {}",
            Thread.currentThread().getName(),
            event.getProductId(), event.getQuantity());

        stockService.decreaseStock(event.getProductId(), event.getQuantity());

        log.info("비동기 재고 차감 처리 완료 - 상품 Id: {}", event.getProductId());

    }
}
