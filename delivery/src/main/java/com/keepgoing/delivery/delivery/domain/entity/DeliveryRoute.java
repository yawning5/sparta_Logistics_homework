package com.keepgoing.delivery.delivery.domain.entity;

import com.keepgoing.delivery.delivery.domain.vo.Distance;
import com.keepgoing.delivery.delivery.domain.vo.Duration;
import com.keepgoing.delivery.delivery.domain.vo.RouteSeq;
import com.keepgoing.delivery.global.exception.BusinessException;
import com.keepgoing.delivery.global.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.UUID;

public class DeliveryRoute {

    private final UUID id;
    private final UUID deliveryId;
    private final RouteSeq routeSeq;
    private final UUID departureHubId;
    private final UUID arrivalHubId;
    private final Distance expectedDistance;
    private final Duration expectedTime;
    private Distance actualDistance;
    private Duration actualTime;
    private DeliveryRouteStatus status;
    private Long deliveryPersonId;

    // soft delete
    private boolean isDeleted = false;
    private LocalDateTime deletedAt;

    // auditing
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private DeliveryRoute(
            UUID deliveryId,
            UUID departureHubId,
            UUID arrivalHubId,
            Distance expectedDistance,
            Duration expectedTime,
            RouteSeq routeSeq
    ) {
        if (deliveryId == null)
            throw new BusinessException(ErrorCode.DELIVERY_INVALID_ID);
        if (departureHubId == null)
            throw new BusinessException(ErrorCode.DELIVERY_INVALID_HUB);
        if (arrivalHubId == null)
            throw new BusinessException(ErrorCode.DELIVERY_INVALID_HUB);
        if (expectedDistance == null || expectedTime == null)
            throw new BusinessException(ErrorCode.DELIVERY_INVALID_DISTANCE_OR_TIME);
        if (routeSeq == null)
            throw new BusinessException(ErrorCode.DELIVERY_INVALID_ROUTE_SEQ);

        this.id = UUID.randomUUID();
        this.deliveryId = deliveryId;
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
            UUID deliveryId,
            UUID departureHubId,
            UUID arrivalHubId,
            Distance expectedDistance,
            Duration expectedTime,
            RouteSeq routeSeq
    ) {
        return new DeliveryRoute(deliveryId, departureHubId, arrivalHubId, expectedDistance, expectedTime, routeSeq);
    }

    private DeliveryRoute(
            UUID id,
            UUID deliveryId,
            UUID departureHubId,
            UUID arrivalHubId,
            Distance expectedDistance,
            Duration expectedTime,
            RouteSeq routeSeq,
            Distance actualDistance,
            Duration actualTime,
            DeliveryRouteStatus status,
            Long deliveryPersonId,
            boolean isDeleted,
            LocalDateTime deletedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.deliveryId = deliveryId;
        this.departureHubId = departureHubId;
        this.arrivalHubId = arrivalHubId;
        this.expectedDistance = expectedDistance;
        this.expectedTime = expectedTime;
        this.routeSeq = routeSeq;
        this.actualDistance = actualDistance;
        this.actualTime = actualTime;
        this.status = status;
        this.deliveryPersonId = deliveryPersonId;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DeliveryRoute reconstruct(
            UUID id,
            UUID deliveryId,
            UUID departureHubId,
            UUID arrivalHubId,
            Distance expectedDistance,
            Duration expectedTime,
            RouteSeq routeSeq,
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
                id,
                deliveryId,
                departureHubId,
                arrivalHubId,
                expectedDistance,
                expectedTime,
                routeSeq,
                actualDistance,
                actualTime,
                status,
                deliveryPersonId,
                isDeleted,
                deletedAt,
                createdAt,
                updatedAt
        );
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getDeliveryId() { return deliveryId; }
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
            throw new BusinessException(ErrorCode.DELIVERY_ROUTE_CAN_MOVE_ONLY_FROM_WAITING);
        this.status = DeliveryRouteStatus.MOVING;
        this.updatedAt = LocalDateTime.now();
    }

    void markArrived() {
        if (status != DeliveryRouteStatus.MOVING)
            throw new BusinessException(ErrorCode.DELIVERY_ROUTE_CAN_ARRIVE_ONLY_FROM_MOVING);
        this.status = DeliveryRouteStatus.ARRIVED;
        this.updatedAt = LocalDateTime.now();
    }

    public void assignDeliveryPerson(Long deliveryPersonId) {
        if (deliveryPersonId == null)
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_REQUIRED_DELIVERY_ID);
        if (this.deliveryPersonId != null)
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_ALREADY_ASSIGNED);
        this.deliveryPersonId = deliveryPersonId;
        this.updatedAt = LocalDateTime.now();
    }

    public void recordActual(Distance actualDistance, Duration actualTime) {
        if (status != DeliveryRouteStatus.ARRIVED)
            throw new BusinessException(ErrorCode.DELIVERY_RECORD_ONLY_AFTER_ARRIVED);
        if (actualDistance == null || actualTime == null)
            throw new BusinessException(ErrorCode.DELIVERY_REQUIRED_ACTUAL_DISTANCE_OR_TIME);
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