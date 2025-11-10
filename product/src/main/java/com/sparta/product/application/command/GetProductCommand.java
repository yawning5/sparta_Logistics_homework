package com.sparta.product.application.command;

import com.sparta.product.domain.vo.UserRole;
import com.sparta.product.infrastructure.security.CustomUserDetails;
import java.util.UUID;

public record GetProductCommand(
    Long userId,
    UUID affiliationId,
    UserRole role,
    String token,
    UUID productId
) {

    public static GetProductCommand of(CustomUserDetails user, UUID productId) {
        return new GetProductCommand(
            user.getUserId(),
            user.getAffiliationId(),
            user.getRole(),
            user.getToken(),
            productId
        );
    }
}
