package com.sparta.hub.inventory.application.service;

import com.sparta.hub.inventory.application.command.AllocateInventoryCommand;
import com.sparta.hub.inventory.application.command.ReceiveInventoryCommand;
import com.sparta.hub.inventory.application.dto.HubInventoryResponse;
import com.sparta.hub.inventory.domain.entity.HubInventory;
import com.sparta.hub.inventory.domain.repository.HubInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HubInventoryService {

    private final HubInventoryRepository hubInventoryRepository;

    public HubInventoryResponse receiveInventory(ReceiveInventoryCommand command) {
        var inventory = hubInventoryRepository
                .findByHubIdAndProductId(command.hubId(), command.productId())
                .map(existing -> {
                    existing.increaseQuantity(command.quantity());
                    return existing;
                })
                .orElseGet(() -> HubInventory.create(command.hubId(), command.productId(), command.quantity()));

        hubInventoryRepository.save(inventory);
        return HubInventoryResponse.from(inventory);
    }

    @Transactional
    public HubInventoryResponse allocateInventory(AllocateInventoryCommand command) {
        var inventory = hubInventoryRepository.findByHubIdAndProductId(command.hubId(), command.productId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 재고가 존재하지 않습니다."));

        if (inventory.getQuantity() < command.reservedQuantity()) {
            throw new IllegalStateException("재고가 부족합니다.");
        }

        inventory.decreaseQuantity(command.reservedQuantity());
        inventory.setStatus("RESERVED");
        hubInventoryRepository.save(inventory);

        return HubInventoryResponse.from(inventory);
    }
}
