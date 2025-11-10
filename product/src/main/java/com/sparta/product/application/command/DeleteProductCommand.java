package com.keepgoing.product.application.command;

import com.keepgoing.product.domain.vo.UserRole;
import com.keepgoing.product.infrastructure.security.CustomUserDetails;
import java.util.UUID;

public record DeleteProductCommand(
    Long userId,
    UUID affiliationId,
    UserRole role,
    String token,
    UUID productId
) {

    public static DeleteProductCommand of(UUID id, CustomUserDetails user) {
        return new DeleteProductCommand(
            user.getUserId(),
            user.getAffiliationId(),
            user.getRole(),
            user.getToken(),
            id
        );
    }
}
