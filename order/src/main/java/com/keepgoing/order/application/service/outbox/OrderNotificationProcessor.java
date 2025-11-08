package com.keepgoing.order.application.service.outbox;

import com.keepgoing.order.domain.outbox.OrderOutbox;
import com.keepgoing.order.domain.outbox.OutBoxState;
import com.keepgoing.order.infrastructure.outbox.OrderOutboxRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j(topic = "OrderNotificationProcessor")
@Service
@RequiredArgsConstructor
public class OrderNotificationProcessor {

    private final OrderOutboxRepository orderOutboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Async("outboxTaskExecutor")
    public void processOutboxTask(Long outboxId) {
        try {
            OrderOutbox outbox = orderOutboxRepository.findById(outboxId)
                .orElse(null);

            if (outbox == null) {
                return;
            }
            switch (outbox.getState()) {
                case NOTIFICATION_PENDING :

                    int updated1 = orderOutboxRepository.claim(
                        outboxId,
                        OutBoxState.NOTIFICATION_PENDING,
                        OutBoxState.NOTIFICATION_COMPLETION_IN_PROGRESS
                    );

                    if (updated1 == 0) {
                        log.debug("이미 처리 중이거나 상태가 일치하지 않습니다. {}", outboxId);
                        return;
                    }

                    try {
                        kafkaTemplate.send("order.notification",
                            String.valueOf(outbox.getAggregateId()), outbox.getPayload())
                            .get(3, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        orderOutboxRepository.updateOrderStateToFailedForNotification(outboxId);
                        log.error("카프카 전송 실패(알림) id={}", outboxId, e);
                        return;
                    }

                    int done = orderOutboxRepository.updateStateToCompletedForNotification(outboxId);
                    if (done != 1) {
                        log.warn("outbox 완료 상태 전이 실패(경쟁/상태 불일치) {}", outboxId);
                        return;
                    }
                    break;

                case DELIVERY_PENDING :

                    int updated2 = orderOutboxRepository.claim(
                        outboxId,
                        OutBoxState.DELIVERY_PENDING,
                        OutBoxState.DELIVERY_COMPLETION_IN_PROGRESS
                    );

                    if (updated2 == 0) {
                        log.debug("이미 처리 중이거나 상태가 일치하지 않습니다. {}", outboxId);
                        return;
                    }

                    try {
                        kafkaTemplate.send("order.delivery",
                                String.valueOf(outbox.getAggregateId()),
                                outbox.getPayload())
                            .get(3, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        orderOutboxRepository.updateOrderStateToFailedForDelivery(outboxId);
                        log.error("카프카 전송 실패(알림) id={}", outboxId, e);
                        return;
                    }

                    int done2 = orderOutboxRepository.updateStateToCompletedForDelivery(outboxId);
                    if (done2 != 1) {
                        log.warn("outbox 완료 상태 전이 실패(경쟁/상태 불일치) {}", outboxId);
                        return;
                    }
                    break;
                default :
                    log.warn("미지원 상태 outboxId={} state={}", outboxId, outbox.getState());
                    return;
            }
        } catch (Exception e) {
            log.error("알림 메시지 생성 실패 id={}", outboxId, e);
        }
    }
}
