package com.keepgoing.order.domain.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_order_outbox",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_order_outbox_event_id", columnNames = "event_id")
    },
    indexes = {
        // Outbox에서 카프카로 전달할 메시지를 찾을 때 사용
        @Index(name = "idx_outbox_state_created_id", columnList = "state, created_at, id"),
        // 특정 주문에 대한 Outbox 이벤트 목록 조회시 사용
        @Index(name = "idx_outbox_aggregate_id", columnList = "aggregate_id")
    }
)
public class OrderOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카프카에 메시지 중복 등록 방지를 위한 ID
    @Column(name = "event_id", nullable = false, updatable = false)
    private UUID eventId;

    // 어떤 애그리거트에서 발생한 이벤트인지 식별
    @Enumerated(EnumType.STRING)
    @Column(name = "aggregate_type", nullable = false, length = 50)
    private AggregateType aggregateType;

    // 해당 이벤트에 속한 도메인 식별자 (주문 ID)
    @Column(name = "aggregate_id", nullable = false, length = 100)
    private String aggregateId;

    // 해당 이벤트 타입 (ORDER_CREATED, ORDER_CANCELD)
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 100)
    private EventType eventType;

    // 카프카에 전달해야 하는 메시지
    @JdbcTypeCode(SqlTypes.JSON) // Hibernate 6 이상
    @Column(name = "payload", nullable = false)
    private String payload;

    // Outbox의 생명주기를 정의
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 100)
    private OutBoxState state;

    // 재시도 횟수를 기록
    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Builder
    private OrderOutbox(UUID eventId, AggregateType aggregateType, String aggregateId, EventType eventType, OutBoxState outBoxState, String payload, LocalDateTime now) {
        this.eventId = eventId;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.state = outBoxState;
        this.payload = payload;
        this.retryCount = 0;
        this.createdAt = now;
        this.updatedAt = now;
        this.version = 0L;
    }

    public static OrderOutbox create(
        UUID eventId, AggregateType aggregateType, String aggregateId, EventType eventType, OutBoxState outBoxState, String payload, LocalDateTime now
    ) {
        return new OrderOutbox(eventId, aggregateType, aggregateId, eventType, outBoxState, payload, now);
    }

    public void increaseRetryCount() {
        retryCount++;
    }

    public void changeOutboxStateToDeliveryFailed() {
        state = OutBoxState.DELIVERY_FAILED;
    }

    public void changeOutboxStateToNotificationFailed() {
        state = OutBoxState.NOTIFICATION_FAILED;
    }

    public void changeOutboxState(OutBoxState outBoxState) {
        state = outBoxState;
    }

    public void resetRetryCount() {
        retryCount = 0;
    }
}
