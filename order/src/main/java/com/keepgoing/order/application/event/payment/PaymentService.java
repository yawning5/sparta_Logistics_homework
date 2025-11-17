package com.keepgoing.order.application.event.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final PaymentRepository paymentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processPayment(UUID orderId, UUID productId, int quantity, int totalPrice) {
        Payment payment = Payment.create(orderId,  productId, quantity, totalPrice);
        payment.complete();

        paymentRepository.save(payment);
        log.info("결제 정보 저장 성공");

        applicationEventPublisher.publishEvent(PaymentCompletedEvent.from(payment));
        log.info("결제 완료 이벤트 발행");
    }
}
