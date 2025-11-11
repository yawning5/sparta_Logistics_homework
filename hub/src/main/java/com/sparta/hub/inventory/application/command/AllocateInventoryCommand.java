package com.sparta.hub.inventory.application.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AllocateInventoryCommand(
        @NotNull UUID hubId,
        @NotNull UUID productId,
        @Min(1) int reservedQuantity
) {}