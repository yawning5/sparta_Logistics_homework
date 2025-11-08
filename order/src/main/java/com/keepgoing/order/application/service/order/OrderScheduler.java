package com.keepgoing.order.application.service.order;

import com.keepgoing.order.domain.order.OrderState;
import com.keepgoing.order.infrastructure.order.OrderRepository;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j(topic = "OrderScheduler")
@Service
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderRepository orderRepository;
    private final OrderProcessor orderProcessor;

    private static final Set<OrderState> PROCESSING_STATES = Set.of(
        OrderState.PENDING_VALIDATION,
        OrderState.PRODUCT_VERIFIED,
        OrderState.AWAITING_PAYMENT,
        OrderState.PAID
    );

    @Scheduled(fixedDelay = 5000)
    public void distributeTasks() {
        Page<UUID> taskIds = orderRepository.findPendingIds(
            PROCESSING_STATES,
            PageRequest.of(0, 100)
        );

        if (!taskIds.hasContent()) {
            return;
        }

        for (UUID orderId : taskIds) {
            try {
                orderProcessor.processTask(orderId);
            } catch (Exception e) {
                log.error("task 스케줄러 작업 위임 실패 {}", orderId, e);
            }
        }
    }

}
