package com.sparta.hub.routes.application.dto;

import com.sparta.hub.routes.domain.entity.HubRoute;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record HubRouteResponse(
        UUID id,
        UUID originHubId,
        UUID destinationHubId,
        int transitMinutes,
        BigDecimal distanceKm
) {
    public static HubRouteResponse from(HubRoute entity) {
        return HubRouteResponse.builder()
                .id(entity.getId())
                .originHubId(entity.getOriginHubId())
                .destinationHubId(entity.getDestinationHubId())
                .transitMinutes(entity.getTransitMinutes())
                .distanceKm(entity.getDistanceKm())
                .build();
    }
}