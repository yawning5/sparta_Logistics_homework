package com.sparta.hub.inventory.application.command;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ShipInventoryCommand(
        @NotNull UUID hubId,
        @NotNull UUID productId
) {}