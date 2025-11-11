package com.sparta.hub.routes.application.command;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record UpdateHubRouteCommand(
        @Positive(message = "운송 시간은 1분 이상이어야 합니다.")
        Integer transitMinutes,
        @Positive(message = "거리는 0보다 커야 합니다.")
        BigDecimal distanceKm
) {}