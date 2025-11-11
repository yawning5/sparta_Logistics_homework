package com.sparta.hub.routes.domain.repository;

import com.sparta.hub.routes.domain.entity.HubRoute;

import java.util.Optional;
import java.util.UUID;

public interface HubRouteRepository {
    HubRoute save(HubRoute route);
    Optional<HubRoute> findById(UUID id);
    Optional<HubRoute> findByOriginHubIdAndDestinationHubId(UUID originHubId, UUID destinationHubId);
    Optional<HubRoute> findByIdAndDeletedAtIsNull(UUID id);
}