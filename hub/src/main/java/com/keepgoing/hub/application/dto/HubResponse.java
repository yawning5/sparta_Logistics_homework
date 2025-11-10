package com.keepgoing.hub.application.dto;

import com.keepgoing.hub.domain.entity.Hub;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HubResponse {
    private UUID id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String hubStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static HubResponse from(Hub hub) {
        return HubResponse.builder()
                .id(hub.getId())
                .name(hub.getName())
                .address(hub.getAddress())
                .latitude(hub.getLatitude())
                .longitude(hub.getLongitude())
                .hubStatus(hub.getHubStatus())
                .createdAt(hub.getCreatedAt())
                .updatedAt(hub.getUpdatedAt())
                .deletedAt(hub.getDeletedAt())
                .build();
    }
}