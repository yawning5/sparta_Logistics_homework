package com.sparta.hub.inventory.presentation.controller;

import com.sparta.hub.inventory.application.command.AdjustInventoryCommand;
import com.sparta.hub.inventory.application.command.AllocateInventoryCommand;
import com.sparta.hub.inventory.application.command.ReceiveInventoryCommand;
import com.sparta.hub.inventory.application.command.ShipInventoryCommand;
import com.sparta.hub.inventory.application.dto.HubInventoryResponse;
import com.sparta.hub.inventory.application.service.HubInventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/v1/inventory")
@RequiredArgsConstructor
public class HubInventoryController {

    private final HubInventoryService hubInventoryService;

    @PostMapping("/receive")
    public ResponseEntity<HubInventoryResponse> receiveInventory(
            @Valid @RequestBody ReceiveInventoryCommand command) {
        return ResponseEntity.ok(hubInventoryService.receiveInventory(command));
    }

    @PostMapping("/allocate")
    public ResponseEntity<HubInventoryResponse> allocateInventory(
            @Valid @RequestBody AllocateInventoryCommand command) {
        return ResponseEntity.ok(hubInventoryService.allocateInventory(command));
    }

    @PostMapping("/ship")
    public ResponseEntity<HubInventoryResponse> shipInventory(
            @Valid @RequestBody ShipInventoryCommand command) {
        return ResponseEntity.ok(hubInventoryService.shipInventory(command));
    }

    @PostMapping("/adjust")
    public ResponseEntity<HubInventoryResponse> adjustInventory(
            @Valid @RequestBody AdjustInventoryCommand command) {
        return ResponseEntity.ok(hubInventoryService.adjustInventory(command));
    }

    @GetMapping("/hubs/{hubId}/products/{productId}")
    public ResponseEntity<HubInventoryResponse> getInventory(
            @PathVariable UUID hubId,
            @PathVariable UUID productId) {
        return ResponseEntity.ok(hubInventoryService.getInventory(hubId, productId));
    }
}