package com.keepgoing.payment.application.service;

import com.keepgoing.payment.application.repo.PaymentRepository;
import com.keepgoing.payment.domain.Payment;
import com.keepgoing.payment.domain.PaymentCompletedEvent;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "PaymentService")
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ApplicationEventPublisher eventPublisher;
    private final PaymentRepository paymentRepository;
    private final Clock clock;

    public void processPayment(UUID orderId, UUID productId, Integer totalPrice, Integer quantity) {
        // 간단한 결제 처리 로직
        Payment payment = Payment.create(orderId, calculateAmount(totalPrice, quantity));

        payment.complete(); // 실무에서는 PG사 연동 등의 복잡한 로직이 들어갑니다

        paymentRepository.save(payment);

        // 결제 완료 이벤트 발행
        eventPublisher.publishEvent(PaymentCompletedEvent.of(
            orderId,
            payment.getId(),
            quantity,
            LocalDateTime.now(clock)
        ));
    }

    private BigDecimal calculateAmount(Integer totalPrice, Integer quantity) {
        if (totalPrice == null || totalPrice <= 0) {
            throw new IllegalArgumentException("totalPrice는 0보다 커야 합니다.");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("quantity는 0보다 커야 합니다.");
        }

        return BigDecimal.valueOf(totalPrice)
            .multiply(BigDecimal.valueOf(quantity));
    }
}


