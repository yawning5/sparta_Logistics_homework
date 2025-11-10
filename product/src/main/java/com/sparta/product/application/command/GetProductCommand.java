package com.keepgoing.product.application.command;

import com.keepgoing.product.domain.vo.UserRole;
import com.keepgoing.product.infrastructure.security.CustomUserDetails;
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
