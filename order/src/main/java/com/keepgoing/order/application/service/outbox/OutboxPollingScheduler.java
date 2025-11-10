package com.keepgoing.order.application.service.outbox;

import com.keepgoing.order.domain.outbox.OutBoxState;
import com.keepgoing.order.infrastructure.outbox.OrderOutboxRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j(topic = "OutboxPollingScheduler")
@Service
@RequiredArgsConstructor
public class OutboxPollingScheduler {

    private final OrderOutboxRepository orderOutboxRepository;
    private final OrderNotificationProcessor orderNotificationProcessor;

    private static final Set<OutBoxState> PENDING_STATES = Set.of(
        OutBoxState.DELIVERY_PENDING,
        OutBoxState.NOTIFICATION_PENDING
    );

    @Scheduled(fixedDelay = 5000)
    public void distributeOutBoxTasks() {
        Page<Long> taskIds = orderOutboxRepository.findPendingOutboxIds(
            PENDING_STATES,
            PageRequest.of(0, 100)
        );

        if (!taskIds.hasContent()) {
            log.debug("수행할 작업이 존재하지 않습니다.");
            return;
        }

        for (Long outboxId : taskIds) {
            try {
                orderNotificationProcessor.processOutboxTask(outboxId);
            } catch (Exception e) {
                log.error("작업 실패 : {}", outboxId, e);
            }
        }
    }
}
