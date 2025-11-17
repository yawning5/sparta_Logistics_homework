package com.sparta.practiceorder.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

// ApplicationEventPublisher를 직접 사용하는 대신 래퍼를 사용하면:
// - 이벤트 발행 전후로 공통 로직(로깅, 이벤트 저장 등)을 추가할 수 있습니다.
// - 나중에 Kafka로 전환할 때 이 클래스만 수정하면 됩니다.
@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(BaseEvent event) {
        log.debug("이벤트 발행 - Type: {}, triggeredBy: {}",
            event.getClass().getSimpleName(),
            event.getTriggeredBy()
        );

        // 실무에서는 여기서 이벤트를 DB에 저장할 수도 있습니다.
        // 이를 "Event Sourcing" 또는 "Event Store" 패턴이라고 합니다.

        applicationEventPublisher.publishEvent(event);
    }
}

