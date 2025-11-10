package com.keepgoing.product.application.command;

import com.keepgoing.product.domain.vo.UserRole;
import com.keepgoing.product.infrastructure.security.CustomUserDetails;
import com.keepgoing.product.presentation.dto.reqeust.UpdateRequestProductDTO;
import java.math.BigInteger;
import java.util.UUID;

public record UpdateProductCommand(
    Long userId,
    UUID affiliationId,
    UserRole role,
    String token,
    UUID productId,
    String productName,
    String productDescription,
    BigInteger productPrice,
    UUID vendorId,
    UUID hubId
) {

    public static UpdateProductCommand of(UUID id, CustomUserDetails user,
        UpdateRequestProductDTO request) {
        return new UpdateProductCommand(
            user.getUserId(),
            user.getAffiliationId(),
            user.getRole(),
            user.getToken(),
            id,
            request.productName(),
            request.productDescription(),
            request.productPrice(),
            request.vendorId(),
            request.hubId()
        );
    }

}
