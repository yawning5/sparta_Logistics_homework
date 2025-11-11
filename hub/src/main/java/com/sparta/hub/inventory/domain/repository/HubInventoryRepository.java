package com.sparta.hub.inventory.domain.repository;

import com.sparta.hub.inventory.domain.entity.HubInventory;
import java.util.Optional;
import java.util.UUID;

public interface HubInventoryRepository {
    Optional<HubInventory> findByHubIdAndProductId(UUID hubId, UUID productId);
    HubInventory save(HubInventory inventory);
}