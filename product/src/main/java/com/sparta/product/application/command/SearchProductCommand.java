package com.sparta.product.application.command;

import java.math.BigInteger;
import java.util.UUID;

public record SearchProductCommand(
    UUID productId,
    String name,
    String description,
    BigInteger minPrice,
    BigInteger maxPrice,
    UUID vendorId,
    UUID hubId
) {

    public static SearchProductCommand of(
        UUID productId,
        String name,
        String description,
        BigInteger minPrice,
        BigInteger maxPrice,
        UUID vendorId,
        UUID hubId
    ) {
        return new SearchProductCommand(
            productId,
            name,
            description,
            minPrice,
            maxPrice,
            vendorId,
            hubId);
    }
}
