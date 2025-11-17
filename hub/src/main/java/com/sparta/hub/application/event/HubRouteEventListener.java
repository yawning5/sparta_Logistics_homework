package com.sparta.hub.application.event;

import com.sparta.hub.routes.application.service.HubRouteService;
import com.sparta.hub.domain.events.HubCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class HubRouteEventListener
        extends AsyncTransactionalEventListener<HubCreatedEvent> {

    private final HubRouteService hubRouteService;

    @Override
    protected void processEvent(HubCreatedEvent event) {
        log.info("허브 기본 라우팅 생성 시작 - 허브 ID: {}", event.getHubId());
        hubRouteService.createDefaultRoutes(event.getHubId());
    }
}