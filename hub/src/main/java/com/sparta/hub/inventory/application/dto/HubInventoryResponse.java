package com.sparta.hub.inventory.application.dto;

import com.sparta.hub.inventory.domain.entity.HubInventory;
import lombok.Builder;
import java.util.UUID;

@Builder
public record HubInventoryResponse(
        UUID id,
        UUID hubId,
        UUID productId,
        String productName,
        int stockQuantity,
        String productStatus
) {
    public static HubInventoryResponse from(HubInventory entity) {
        return HubInventoryResponse.builder()
                .id(entity.getId())
                .hubId(entity.getHubId())
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .stockQuantity(entity.getQuantity())
                .productStatus(entity.getStatus())
                .build();
    }
}
