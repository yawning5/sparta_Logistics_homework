package com.sparta.hub.inventory.infrastructure.repository;

import com.sparta.hub.inventory.domain.entity.HubInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaHubInventoryRepository extends JpaRepository<HubInventory, UUID> {
    Optional<HubInventory> findByHubIdAndProductId(UUID hubId, UUID productId);
}
