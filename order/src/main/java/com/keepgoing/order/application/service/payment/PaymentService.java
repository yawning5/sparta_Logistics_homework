package com.keepgoing.order.application.service.payment;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService {

    public void processPayment(UUID orderId, UUID productId, int quantity) {

        log.info("PaymentService processPayment 호출 - 주문 ID: {}, 상품 ID: {}, 수량: {}",
            orderId,
            productId,
            quantity
        );
    }
}
