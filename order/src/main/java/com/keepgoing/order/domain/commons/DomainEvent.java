package com.keepgoing.order.domain.commons;

import java.time.LocalDateTime;
import java.util.UUID;

// 1. 공통 이벤트 인터페이스
// 모든 도메인 이벤트가 구현해야 할 공통 인터페이스입니다.
public interface DomainEvent {
    // 어떤 엔티티(Aggregate)에서 발생한 이벤트인지
    UUID getAggregateId();

    // 이벤트가 발생한 시점
    LocalDateTime getOccurredAt();
}
