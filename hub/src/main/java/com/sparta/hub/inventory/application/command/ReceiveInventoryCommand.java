package com.sparta.hub.inventory.application.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReceiveInventoryCommand(
        @NotNull UUID hubId,
        @NotNull UUID productId,
        @Min(1) int quantity
) {}
