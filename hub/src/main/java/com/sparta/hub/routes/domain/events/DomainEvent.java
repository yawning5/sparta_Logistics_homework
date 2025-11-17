package com.sparta.hub.routes.domain.events;

import lombok.Getter;
import java.util.UUID;
import java.time.LocalDateTime;

@Getter
public abstract class DomainEvent {
    private final UUID aggregateId;
    private final LocalDateTime occurredAt = LocalDateTime.now();

    protected DomainEvent(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }
}