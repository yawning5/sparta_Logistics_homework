package com.sparta.hub.routes.domain.events;

import java.util.UUID;
import com.sparta.hub.domain.entity.Hub;
import lombok.Getter;

@Getter
public class HubCreatedEvent extends DomainEvent {

    private final UUID hubId;

    private HubCreatedEvent(UUID hubId) {
        super(hubId);
        this.hubId = hubId;
    }

    public static HubCreatedEvent of(Hub hub) {
        return new HubCreatedEvent(hub.getId());
    }
}