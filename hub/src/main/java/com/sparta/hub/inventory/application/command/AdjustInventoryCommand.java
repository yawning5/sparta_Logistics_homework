package com.sparta.hub.inventory.application.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AdjustInventoryCommand(
        @NotNull UUID hubId,
        @NotNull UUID productId,
        @Min(1) int quantity,
        @NotBlank String action // "CANCEL" or "ADJUST"
) {}
