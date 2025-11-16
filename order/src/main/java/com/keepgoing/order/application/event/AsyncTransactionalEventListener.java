package com.keepgoing.order.application.event;

import com.keepgoing.order.domain.commons.DomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
public abstract class AsyncTransactionalEventListener<T extends DomainEvent> {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEvent(T event) {
        try {
            log.info("이벤트 처리 시작 - Type: {}, AggregateId: {}",
                event.getClass().getSimpleName(),
                event.getAggregateId()
            );

            // 실제 비즈니스 로직은 하위 클래스에서 구현
            processEvent(event);

            log.info("이벤트 처리 완료 - Type: {}", event.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("이벤트 처리 실패 - Type: {}, Error: {}",
                event.getClass().getSimpleName(),
                e.getMessage()
            );
            handleException(event, e);
        }
    }

    // 하위 클래스에서 구현할 추상 메서드
    protected abstract void processEvent(T event);

    // 필요 시 하위 클래스에서 오버라이드 가능
    protected void handleException(T event, Exception e) {
        // 기본 예외 처리 로직
        // 실무에서는 재시도, 알림 발송 등을 구현할 수 있습니다.
    }
}
