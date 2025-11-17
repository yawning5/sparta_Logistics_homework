package com.sparta.hub.application.event;

import com.sparta.hub.domain.events.HubCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HubEventListener {

    @EventListener
    public void handle(HubCreatedEvent event) {
        log.info("허브 생성 이벤트 수신 - 허브 ID: {}", event.getHubId());
    }
}