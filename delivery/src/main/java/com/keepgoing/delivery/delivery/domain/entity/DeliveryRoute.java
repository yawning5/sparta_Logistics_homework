package com.keepgoing.delivery.delivery.domain.entity;

import java.util.Objects;
import java.util.UUID;

public class DeliveryRoute {

    private final UUID id;
    private final RouteSeq routeSeq;
    private final UUID departureHubId;
    private final UUID arrivalHubId;
    private final Distance expectedDistance;
    private final Duration expectedTime;
    private Distance actualDistance;
    private Duration actualTime;
    private DeliveryRouteStatus status;
    private Long deliveryPersonId;

    private DeliveryRoute(
            UUID departureHubId,
            UUID arrivalHubId,
            Distance expectedDistance,
            Duration expectedTime,
            RouteSeq routeSeq
    ) {
        // 필수값 검증
        if (departureHubId == null)
            throw new IllegalArgumentException("출발 허브 ID는 필수입니다.");
        if (arrivalHubId == null)
            throw new IllegalArgumentException("도착 허브 ID는 필수입니다.");
        if (expectedDistance == null || expectedTime == null)
            throw new IllegalArgumentException("거리와 소요 시간은 필수입니다.");
        if (routeSeq == null)
            throw new IllegalArgumentException("경로 순서는 필수입니다.");

        this.id = UUID.randomUUID();
        this.departureHubId = departureHubId;
        this.arrivalHubId = arrivalHubId;
        this.expectedDistance = expectedDistance;
        this.expectedTime = expectedTime;
        this.routeSeq = routeSeq;
        this.status = DeliveryRouteStatus.WAITING;
    }

    public static DeliveryRoute create(UUID departureHubId,
                                       UUID arrivalHubId,
                                       Distance expectedDistance,
                                       Duration expectedTime,
                                       RouteSeq routeSeq) {
        return new DeliveryRoute(departureHubId, arrivalHubId, expectedDistance, expectedTime, routeSeq);
    }

    public DeliveryRouteStatus getStatus() {
        return status;
    }

}
