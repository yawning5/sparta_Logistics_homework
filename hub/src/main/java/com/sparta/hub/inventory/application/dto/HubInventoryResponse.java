package com.sparta.hub.inventory.application.dto;

import com.sparta.hub.inventory.domain.entity.HubInventory;
import lombok.Builder;

import java.util.UUID;

@Builder
public record HubInventoryResponse(
        UUID id,
        UUID hubId,
        UUID productId,
        int quantity,
        String status
) {
    public static HubInventoryResponse from(HubInventory entity) {
        return HubInventoryResponse.builder()
                .id(entity.getId())
                .hubId(entity.getHubId())
                .productId(entity.getProductId())
                .quantity(entity.getQuantity())
                .status(entity.getStatus())
                .build();
    }
}
