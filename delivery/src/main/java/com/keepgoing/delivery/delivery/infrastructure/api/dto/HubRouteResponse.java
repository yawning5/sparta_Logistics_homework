package com.keepgoing.delivery.delivery.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record HubRouteResponse(
    @JsonProperty("id") UUID routeId,
    @JsonProperty("originHubId") UUID departureHubId,
    @JsonProperty("destinationHubId") UUID arrivalHubId,
    @JsonProperty("distanceKm") Double expectedDistance,
    @JsonProperty("transitMinutes") Integer expectedTime
) {

}