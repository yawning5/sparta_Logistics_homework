package com.sparta.hub.inventory.presentation.controller;

import com.sparta.hub.inventory.application.command.AllocateInventoryCommand;
import com.sparta.hub.inventory.application.command.ReceiveInventoryCommand;
import com.sparta.hub.inventory.application.dto.HubInventoryResponse;
import com.sparta.hub.inventory.application.service.HubInventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}