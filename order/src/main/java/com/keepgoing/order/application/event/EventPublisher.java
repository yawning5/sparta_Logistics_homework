package com.keepgoing.order.application.event;

import com.keepgoing.order.domain.commons.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(DomainEvent event) {
        log.debug("이벤트 발행 - Type: {}, AggregateId: {}",
            event.getClass().getSimpleName(),
            event.getAggregateId()
        );

        // 실무에서는 여기서 이벤트를 DB에 저장할 수도 있습니다.
        // 이를 "Event Sourcing" 또는 "Event Store" 패턴이라고 합니다.

        applicationEventPublisher.publishEvent(event);
    }
}
