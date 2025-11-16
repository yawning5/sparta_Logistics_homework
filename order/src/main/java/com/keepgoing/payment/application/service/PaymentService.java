package com.keepgoing.payment.application.service;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j(topic = "PaymentService")
@Service
public class PaymentService {

    public void processPayment(UUID orderId, UUID productId) {
        log.info("결제 처리 프로세스 실행 주문 ID {}, 상품 ID {}", orderId, productId);
    }
}
