package com.keepgoing.delivery.delivery.application.dto.request;

import com.keepgoing.delivery.delivery.domain.vo.Distance;
import com.keepgoing.delivery.delivery.domain.vo.Duration;

public record CompleteRouteRequest(
        int routeSeq,
        double actualDistanceKm,
        int actualTimeMinutes
) {
    public Distance toDistance() {
        return new Distance(actualDistanceKm);
    }

    public Duration toDuration() {
        return new Duration(actualTimeMinutes);
    }
}