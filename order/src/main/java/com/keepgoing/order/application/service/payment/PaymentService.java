package com.keepgoing.order.application.service.payment;

import com.keepgoing.order.application.event.EventPublisher;
import com.keepgoing.order.domain.payment.Payment;
import com.keepgoing.order.domain.payment.event.PaymentCompletedEvent;
import com.keepgoing.order.infrastructure.payment.PaymentRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public void processPayment(UUID orderId, UUID productId, int quantity) {

        log.info("결제 프로세스 시작");
        // 간단한 결제 처리 로직
        Payment payment = Payment.create(orderId, calculateAmount(productId, quantity));

        if (quantity == 50) {
            log.info("어거지로 만든 결제 실패 로직");
            payment.fail();
            paymentRepository.save(payment);
            throw new RuntimeException("결제 실패");
        }

        payment.complete(); // 실무에서는 PG사 연동 등의 복잡한 로직이 들어갑니다

        paymentRepository.save(payment);

        // 결제 완료 이벤트 발행
        eventPublisher.publish(PaymentCompletedEvent.of(payment, productId, quantity));
        log.info("결제 프로세스 끝");
    }

    private BigDecimal calculateAmount(UUID productId, int quantity) {
        return BigDecimal.valueOf(1000L * quantity); // 예시
    }
}
