package com.keepgoing.product.presentation.dto.response;

import com.keepgoing.product.application.dto.ProductResult;
import java.math.BigInteger;
import java.util.UUID;

public record ProductResponseDTO(
    UUID id,
    String productName,
    String productDescription,
    BigInteger productPrice,
    UUID vendorId,
    UUID hubId
) {

    public static ProductResponseDTO from(ProductResult productResult) {
        return new ProductResponseDTO(
            productResult.id(),
            productResult.productName(),
            productResult.productDescription(),
            productResult.productPrice(),
            productResult.vendorId(),
            productResult.hubId()
        );
    }
}
