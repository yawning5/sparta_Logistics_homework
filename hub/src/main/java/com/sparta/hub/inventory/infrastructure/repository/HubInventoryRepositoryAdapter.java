package com.sparta.hub.inventory.infrastructure.repository;

import com.sparta.hub.inventory.domain.entity.HubInventory;
import com.sparta.hub.inventory.domain.repository.HubInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HubInventoryRepositoryAdapter implements HubInventoryRepository {

    private final JpaHubInventoryRepository jpaRepository;

    @Override
    public Optional<HubInventory> findByHubIdAndProductId(UUID hubId, UUID productId) {
        return jpaRepository.findByHubIdAndProductId(hubId, productId);
    }

    @Override
    public HubInventory save(HubInventory inventory) {
        return jpaRepository.save(inventory);
    }
}
