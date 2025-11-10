package com.keepgoing.delivery.delivery.application.dto.response;

import com.keepgoing.delivery.delivery.domain.entity.DeliveryRoute;
import com.keepgoing.delivery.delivery.domain.entity.DeliveryRouteStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryRouteResponse(
        UUID id,
        UUID deliveryId,
        int routeSeq,
        UUID departureHubId,
        UUID arrivalHubId,
        double expectedDistanceKm,
        int expectedTimeMinutes,
        Double actualDistanceKm,
        Integer actualTimeMinutes,
        DeliveryRouteStatus status,
        Long deliveryPersonId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static DeliveryRouteResponse from(DeliveryRoute route) {
        return new DeliveryRouteResponse(
                route.getId(),
                route.getDeliveryId(),
                route.getRouteSeq().value(),
                route.getDepartureHubId(),
                route.getArrivalHubId(),
                route.getExpectedDistance().km(),
                route.getExpectedTime().minutes(),
                route.getActualDistance() != null ? route.getActualDistance().km() : null,
                route.getActualTime() != null ? route.getActualTime().minutes() : null,
                route.getStatus(),
                route.getDeliveryPersonId(),
                route.getCreatedAt(),
                route.getUpdatedAt()
        );
    }
}