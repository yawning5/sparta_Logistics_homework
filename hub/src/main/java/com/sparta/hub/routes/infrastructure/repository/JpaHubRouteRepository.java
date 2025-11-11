package com.sparta.hub.routes.infrastructure.repository;

import com.sparta.hub.routes.domain.entity.HubRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaHubRouteRepository extends JpaRepository<HubRoute, UUID>, JpaSpecificationExecutor<HubRoute> {
    Optional<HubRoute> findByOriginHubIdAndDestinationHubId(UUID originHubId, UUID destinationHubId);
    Optional<HubRoute> findByIdAndDeletedAtIsNull(UUID id);
}