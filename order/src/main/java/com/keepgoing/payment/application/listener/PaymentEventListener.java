package com.keepgoing.payment.application.listener;

import com.keepgoing.order.domain.event.OrderCreatedEvent;
import com.keepgoing.payment.application.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j(topic = "PaymentEventListener")
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final PaymentService paymentService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("주문 생성 이벤트 수신 후, 결제 처리 실행 주문 ID : {}", event.getOrderId());

        paymentService.processPayment(
            event.getOrderId(),
            event.getProductId()
        );

        log.info("결제 처리 완료 주문 ID : {}", event.getOrderId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleOrderCreatedFail(OrderCreatedEvent event) {
        log.error("주문 생성에 실패하여 이벤트가 발행되지 않았습니다.");
    }
}
