package com.sparta.product.application.command;

import com.sparta.product.domain.vo.UserRole;
import com.sparta.product.infrastructure.security.CustomUserDetails;
import com.sparta.product.presentation.dto.reqeust.CreateProductRequestDTO;
import java.math.BigInteger;
import java.util.UUID;

public record CreateProductCommand(
    Long userId,
    UUID affiliationId,
    UserRole role,
    String token,
    String productName,
    String productDescription,
    BigInteger productPrice,
    UUID vendorId,
    UUID hubId
) {

    public static CreateProductCommand of(CustomUserDetails user,
        CreateProductRequestDTO request) {
        return new CreateProductCommand(
            user.getUserId(),
            user.getAffiliationId(),
            user.getRole(),
            user.getToken(),
            request.productName(),
            request.productDescription(),
            request.productPrice(),
            request.vendorId(),
            request.hubId()
        );
    }
}
