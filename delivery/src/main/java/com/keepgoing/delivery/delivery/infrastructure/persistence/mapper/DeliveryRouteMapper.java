package com.keepgoing.delivery.delivery.infrastructure.persistence.mapper;

import com.keepgoing.delivery.delivery.domain.entity.DeliveryRoute;
import com.keepgoing.delivery.delivery.domain.vo.Distance;
import com.keepgoing.delivery.delivery.domain.vo.Duration;
import com.keepgoing.delivery.delivery.domain.vo.RouteSeq;
import com.keepgoing.delivery.delivery.infrastructure.persistence.entity.DeliveryRouteEntity;
import org.springframework.stereotype.Component;

@Component
public class DeliveryRouteMapper {

    public DeliveryRouteEntity toJpaEntity(DeliveryRoute domain) {
        return new DeliveryRouteEntity(
                domain.getId(),
                domain.getDeliveryId(),
                domain.getRouteSeq().value(),
                domain.getDepartureHubId(),
                domain.getArrivalHubId(),
                domain.getExpectedDistance().km(),
                domain.getExpectedTime().minutes(),
                domain.getActualDistance() != null ? domain.getActualDistance().km() : null,
                domain.getActualTime() != null ? domain.getActualTime().minutes() : null,
                domain.getStatus(),
                domain.getDeliveryPersonId(),
                domain.isDeleted(),
                domain.getDeletedAt(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }

    public DeliveryRoute toDomainEntity(DeliveryRouteEntity jpa) {
        Distance actualDistance = jpa.getActualDistanceKm() != null
                ? new Distance(jpa.getActualDistanceKm())
                : null;

        Duration actualTime = jpa.getActualTimeMinutes() != null
                ? new Duration(jpa.getActualTimeMinutes())
                : null;

        return DeliveryRoute.reconstruct(
                jpa.getId(),
                jpa.getDeliveryId(),
                new RouteSeq(jpa.getRouteSeq()),
                jpa.getDepartureHubId(),
                jpa.getArrivalHubId(),
                new Distance(jpa.getExpectedDistanceKm()),
                new Duration(jpa.getExpectedTimeMinutes()),
                actualDistance,
                actualTime,
                jpa.getStatus(),
                jpa.getDeliveryPersonId(),
                jpa.isDeleted(),
                jpa.getDeletedAt(),
                jpa.getCreatedAt(),
                jpa.getUpdatedAt()
        );
    }
}