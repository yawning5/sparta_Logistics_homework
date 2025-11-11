package com.sparta.hub.inventory.application.service;

import com.sparta.hub.inventory.application.command.AdjustInventoryCommand;
import com.sparta.hub.inventory.application.command.AllocateInventoryCommand;
import com.sparta.hub.inventory.application.command.ReceiveInventoryCommand;
import com.sparta.hub.inventory.application.command.ShipInventoryCommand;
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

    @Transactional
    public HubInventoryResponse shipInventory(ShipInventoryCommand command) {
        var inventory = hubInventoryRepository.findByHubIdAndProductId(command.hubId(), command.productId())
                .orElseThrow(() -> new IllegalArgumentException("재고를 찾을 수 없습니다."));

        if (!"RESERVED".equals(inventory.getStatus())) {
            throw new IllegalStateException("출고 대기 상태가 아닙니다.");
        }

        inventory.setStatus("SHIPPED");
        hubInventoryRepository.save(inventory);

        return HubInventoryResponse.from(inventory);
    }

    @Transactional
    public HubInventoryResponse adjustInventory(AdjustInventoryCommand command) {
        var inventory = hubInventoryRepository.findByHubIdAndProductId(command.hubId(), command.productId())
                .orElseThrow(() -> new IllegalArgumentException("재고를 찾을 수 없습니다."));

        switch (command.action().toUpperCase()) {
            case "CANCEL" -> {
                inventory.increaseQuantity(command.quantity());
                inventory.setStatus("AVAILABLE");
            }
            case "ADJUST" -> {
                inventory.setQuantity(command.quantity());
                inventory.setStatus("ADJUSTED");
            }
            default -> throw new IllegalArgumentException("올바르지 않은 action 값입니다. (CANCEL 또는 ADJUST)");
        }

        hubInventoryRepository.save(inventory);
        return HubInventoryResponse.from(inventory);
    }
}
