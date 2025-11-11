package com.sparta.hub.routes.infrastructure.repository;

import com.sparta.hub.routes.domain.entity.HubRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaHubRouteRepository extends JpaRepository<HubRoute, UUID>, JpaSpecificationExecutor<HubRoute> {
    // 출발 허브와 도착 허브로 경로 단건 조회 (중복체크용)
    Optional<HubRoute> findByOriginHubIdAndDestinationHubId(UUID originHubId, UUID destinationHubId);
    //Soft Delete 되지 않은 ID 기준 단건 조회
    Optional<HubRoute> findByIdAndDeletedAtIsNull(UUID id);
}