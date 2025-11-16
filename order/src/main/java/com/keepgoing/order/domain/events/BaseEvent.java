package com.keepgoing.order.domain.events;

import java.lang.StackWalker.Option;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class BaseEvent {
    private final UUID eventId;
    private final LocalDateTime createdAt;
    private final String triggeredBy;

    protected BaseEvent() {
        this.eventId = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.triggeredBy = StackWalker
            .getInstance(Option.RETAIN_CLASS_REFERENCE)
            .walk(frames -> frames
                .skip(1)// BaseEvent 생성자를 호출한 실제 메서드
                .findFirst()
                .map(frame -> frame.getClassName() + "#" + frame.getMethodName())
                .orElse("unknown"));
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public UUID getEventId() {
        return eventId;
    }
}
