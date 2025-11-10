package com.sparta.product.application.command;

import com.sparta.product.domain.vo.UserRole;
import com.sparta.product.infrastructure.security.CustomUserDetails;
import java.math.BigInteger;
import java.util.UUID;

public record SearchProductCommand(
    Long userId,
    UUID affiliationId,
    UserRole role,
    UUID productId,
    String name,
    String description,
    BigInteger minPrice,
    BigInteger maxPrice,
    UUID vendorId,
    UUID hubId
) {
    public static SearchProductCommand of(CustomUserDetails user,
        UUID productId,
        String name,
        String description,
        BigInteger minPrice,
        BigInteger maxPrice,
        UUID vendorId,
        UUID hubId) {
        return new SearchProductCommand(
            user.getUserId(),
            user.getAffiliationId(),
            user.getRole(),
            productId,
            name,
            description,
            minPrice,
            maxPrice,
            vendorId,
            hubId
        );
    }

    public static SearchProductCommand of(
        Long userId,
        UUID affiliationId,
        UserRole role,
        UUID productId,
        String name,
        String description,
        BigInteger minPrice,
        BigInteger maxPrice,
        UUID vendorId,
        UUID hubId) {
        return new SearchProductCommand(
            userId,
            affiliationId,
            role,
            productId,
            name,
            description,
            minPrice,
            maxPrice,
            vendorId,
            hubId
        );
    }
}
