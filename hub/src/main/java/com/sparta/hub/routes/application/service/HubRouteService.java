package com.sparta.hub.routes.application.service;

import com.sparta.hub.routes.application.command.CreateHubRouteCommand;
import com.sparta.hub.routes.application.dto.HubRouteResponse;
import com.sparta.hub.routes.domain.entity.HubRoute;
import com.sparta.hub.routes.domain.repository.HubRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HubRouteService {

    private final HubRouteRepository hubRouteRepository;

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
}