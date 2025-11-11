package com.sparta.hub.routes.application.command;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateHubRouteCommand(
        @NotNull UUID originHubId,
        @NotNull UUID destinationHubId,
        @Positive int transitMinutes,
        @DecimalMin("0.01") BigDecimal distanceKm
) {
    public void validate() {
        if (originHubId.equals(destinationHubId)) {
            throw new IllegalArgumentException("출발 허브와 도착 허브가 같을 수 없습니다.");
        }
    }
}