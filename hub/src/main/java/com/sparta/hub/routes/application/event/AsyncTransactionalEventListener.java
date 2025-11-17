package com.sparta.hub.routes.application.event;

import com.sparta.hub.routes.domain.events.DomainEvent;
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
            log.info("비동기 이벤트 처리 시작 - {}", event.getClass().getSimpleName());
            processEvent(event);
        } catch (Exception e) {
            log.error("에러 발생: {}", e.getMessage());
            handleException(event, e);
        }
    }

    protected abstract void processEvent(T event);

    protected void handleException(T event, Exception e) {}
}