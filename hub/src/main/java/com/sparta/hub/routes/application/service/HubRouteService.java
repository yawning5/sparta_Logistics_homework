package com.sparta.hub.routes.application.service;

import com.sparta.hub.routes.application.command.CreateHubRouteCommand;
import com.sparta.hub.routes.application.command.UpdateHubRouteCommand;
import com.sparta.hub.routes.application.dto.HubRouteResponse;
import com.sparta.hub.routes.application.query.HubRouteSearchQuery;
import com.sparta.hub.routes.domain.entity.HubRoute;
import com.sparta.hub.routes.domain.repository.HubRouteRepository;
import com.sparta.hub.routes.infrastructure.repository.JpaHubRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class HubRouteService {

    private final HubRouteRepository hubRouteRepository;
    private final JpaHubRouteRepository jpaHubRouteRepository;

    public HubRouteResponse createRoute(CreateHubRouteCommand command) {
        command.validate();

        hubRouteRepository.findByOriginHubIdAndDestinationHubId(command.originHubId(), command.destinationHubId())
                .ifPresent(r -> {
                    throw new IllegalStateException("이미 동일한 경로가 존재합니다.");
                });

        HubRoute route = HubRoute.create(
                command.originHubId(),
                command.destinationHubId(),
                command.transitMinutes(),
                command.distanceKm()
        );

        hubRouteRepository.save(route);
        return HubRouteResponse.from(route);
    }

    @Transactional(readOnly = true)
    public HubRouteResponse getRoute(UUID id) {
        HubRoute route = hubRouteRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("허브 이동 경로를 찾을 수 없습니다."));
        return HubRouteResponse.from(route);
    }

    @Transactional(readOnly = true)
    public Page<HubRouteResponse> searchRoutes(HubRouteSearchQuery query) {
        Specification<HubRoute> spec = Specification.allOf(isNotDeleted());

        if (query.originHubId() != null) {
            spec = spec.and(originHubEq(query.originHubId()));
        }
        if (query.destinationHubId() != null) {
            spec = spec.and(destinationHubEq(query.destinationHubId()));
        }
        if (query.maxTransitMinutes() != null) {
            spec = spec.and(transitMinutesLe(query.maxTransitMinutes()));
        }
        if (query.maxDistanceKm() != null) {
            spec = spec.and(distanceLe(query.maxDistanceKm()));
        }

        return jpaHubRouteRepository.findAll(spec, query.pageable())
                .map(HubRouteResponse::from);
    }

    @Transactional
    public HubRouteResponse updateRoute(UUID id, UpdateHubRouteCommand command) {
        HubRoute route = hubRouteRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("허브 이동 경로를 찾을 수 없습니다."));

        if (command.transitMinutes() != null) {
            route.updateTransitMinutes(command.transitMinutes());
        }
        if (command.distanceKm() != null) {
            route.updateDistanceKm(command.distanceKm());
        }

        hubRouteRepository.save(route);
        return HubRouteResponse.from(route);
    }

    @Transactional
    public void deleteRoute(UUID id) {
        HubRoute route = hubRouteRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("허브 이동 경로를 찾을 수 없습니다."));

        route.delete();
        hubRouteRepository.save(route);
    }

    // ==================== Specification ====================
    private Specification<HubRoute> isNotDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    private Specification<HubRoute> originHubEq(UUID originHubId) {
        return (root, query, cb) -> cb.equal(root.get("originHubId"), originHubId);
    }

    private Specification<HubRoute> destinationHubEq(UUID destinationHubId) {
        return (root, query, cb) -> cb.equal(root.get("destinationHubId"), destinationHubId);
    }

    private Specification<HubRoute> transitMinutesLe(Integer maxMinutes) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("transitMinutes"), maxMinutes);
    }

    private Specification<HubRoute> distanceLe(BigDecimal maxDistanceKm) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("distanceKm"), maxDistanceKm);
    }


}