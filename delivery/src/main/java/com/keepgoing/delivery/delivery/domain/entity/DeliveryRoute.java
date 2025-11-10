package com.keepgoing.delivery.delivery.domain.entity;

import com.keepgoing.delivery.delivery.domain.vo.Distance;
import com.keepgoing.delivery.delivery.domain.vo.Duration;
import com.keepgoing.delivery.delivery.domain.vo.RouteSeq;

import java.time.LocalDateTime;
import java.util.UUID;

public class DeliveryRoute {

    private final UUID id;
    private final UUID deliveryId;  // 추가: 배송 ID 참조
    private final RouteSeq routeSeq;
    private final UUID departureHubId;
    private final UUID arrivalHubId;
    private final Distance expectedDistance;
    private final Duration expectedTime;
    private Distance actualDistance;
    private Duration actualTime;
    private DeliveryRouteStatus status;
    private Long deliveryPersonId;

    // soft delete 필드
    private boolean isDeleted = false;
    private LocalDateTime deletedAt;

    // auditing 필드 (선택사항)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private DeliveryRoute(
            UUID deliveryId,  // 추가
            UUID departureHubId,
            UUID arrivalHubId,
            Distance expectedDistance,
            Duration expectedTime,
            RouteSeq routeSeq
    ) {
        if (deliveryId == null)
            throw new IllegalArgumentException("배송 ID는 필수입니다.");
        if (departureHubId == null)
            throw new IllegalArgumentException("출발 허브 ID는 필수입니다.");
        if (arrivalHubId == null)
            throw new IllegalArgumentException("도착 허브 ID는 필수입니다.");
        if (expectedDistance == null || expectedTime == null)
            throw new IllegalArgumentException("거리와 소요 시간은 필수입니다.");
        if (routeSeq == null)
            throw new IllegalArgumentException("경로 순서는 필수입니다.");

        this.id = UUID.randomUUID();
        this.deliveryId = deliveryId;  // 추가
        this.departureHubId = departureHubId;
        this.arrivalHubId = arrivalHubId;
        this.expectedDistance = expectedDistance;
        this.expectedTime = expectedTime;
        this.routeSeq = routeSeq;
        this.status = DeliveryRouteStatus.WAITING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static DeliveryRoute create(
            UUID deliveryId,  // 추가
            UUID departureHubId,
            UUID arrivalHubId,
            Distance expectedDistance,
            Duration expectedTime,
            RouteSeq routeSeq
    ) {
        return new DeliveryRoute(deliveryId, departureHubId, arrivalHubId, expectedDistance, expectedTime, routeSeq);
    }

    public static DeliveryRoute reconstruct(
            UUID id,
            UUID deliveryId,
            RouteSeq routeSeq,
            UUID departureHubId,
            UUID arrivalHubId,
            Distance expectedDistance,
            Duration expectedTime,
            Distance actualDistance,
            Duration actualTime,
            DeliveryRouteStatus status,
            Long deliveryPersonId,
            boolean isDeleted,
            LocalDateTime deletedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new DeliveryRoute(
                deliveryId,
                departureHubId,
                arrivalHubId,
                expectedDistance,
                expectedTime,
                routeSeq
        );
    }
    // Getters
    public UUID getId() { return id; }
    public UUID getDeliveryId() { return deliveryId; }  // 추가
    public RouteSeq getRouteSeq() { return routeSeq; }
    public UUID getDepartureHubId() { return departureHubId; }
    public UUID getArrivalHubId() { return arrivalHubId; }
    public Distance getExpectedDistance() { return expectedDistance; }
    public Duration getExpectedTime() { return expectedTime; }
    public Distance getActualDistance() { return actualDistance; }
    public Duration getActualTime() { return actualTime; }
    public DeliveryRouteStatus getStatus() { return status; }
    public Long getDeliveryPersonId() { return deliveryPersonId; }
    public boolean isDeleted() { return isDeleted; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    void startMoving() {
        if (status != DeliveryRouteStatus.WAITING)
            throw new IllegalStateException("WAITING 상태에서만 이동할 수 있습니다.");
        this.status = DeliveryRouteStatus.MOVING;
        this.updatedAt = LocalDateTime.now();
    }

    void markArrived() {
        if (status != DeliveryRouteStatus.MOVING)
            throw new IllegalStateException("MOVING 상태에서만 도착 처리할 수 있습니다.");
        this.status = DeliveryRouteStatus.ARRIVED;
        this.updatedAt = LocalDateTime.now();
    }

    public void assignDeliveryPerson(Long deliveryPersonId) {
        if (deliveryPersonId == null)
            throw new IllegalArgumentException("배달원 ID는 필수입니다.");
        if (this.deliveryPersonId != null)
            throw new IllegalStateException("이미 배달원이 지정되어 있습니다.");
        this.deliveryPersonId = deliveryPersonId;
        this.updatedAt = LocalDateTime.now();
    }

    public void recordActual(Distance actualDistance, Duration actualTime) {
        if (status != DeliveryRouteStatus.ARRIVED)
            throw new IllegalStateException("ARRIVED 상태에서만 실제 거리/시간을 기록할 수 있습니다.");
        if (actualDistance == null || actualTime == null)
            throw new IllegalArgumentException("실제 거리와 시간은 필수입니다.");
        this.actualDistance = actualDistance;
        this.actualTime = actualTime;
        this.updatedAt = LocalDateTime.now();
    }

    public void markDeleted() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}