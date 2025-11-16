package com.keepgoing.payment.application.listener;

import com.keepgoing.order.domain.event.OrderCreatedEvent;
import com.keepgoing.payment.application.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j(topic = "PaymentEventListener")
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final PaymentService paymentService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleOrderCreatedFail(OrderCreatedEvent event) {
        log.error("주문 생성에 실패하여 이벤트가 발행되지 않았습니다.");
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("주문 생성 이벤트 수신 후, 비동기 결제 처리 진행 주문 ID : {}, Thread : {}"
            , event.getOrderId(), Thread.currentThread().getName());

        /**
         * 결제 레코드 생성 (생략)
         * PG사와 통신하여 실제 결제 진행 (생략)
         * 그 결과를 DB에 저장 (생략)
         * 결제 완료 이벤트 발행
         */
        paymentService.processPayment(
            event.getOrderId(),
            event.getProductId(),
            event.getTotalPrice(),
            event.getQuantity()
        );

        log.info("주문 생성 이벤트 수신 후, 비동기 결제 처리 완료 주문 ID : {}", event.getOrderId());
    }
}
