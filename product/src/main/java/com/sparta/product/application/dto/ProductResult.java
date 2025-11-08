package com.sparta.product.application.dto;

import com.sparta.product.domain.entity.Product;
import java.math.BigInteger;
import java.util.UUID;

public record ProductResult(
    UUID id,
    String productName,
    String productDescription,
    BigInteger productPrice,
    UUID vendorId,
    UUID hubId
) {

    public static ProductResult from(Product product) {
        return new ProductResult(
            product.getId(),
            product.getProductName(),
            product.getProductDescription(),
            product.getProductPrice(),
            product.getVendorId().getId(),
            product.getHubId().getId()
        );
    }
}
