package com.sparta.product.presentation.dto.reqeust;

import java.math.BigInteger;
import java.util.UUID;

public record SearchProductRequest(
    UUID productId,
    String name,
    String description,
    BigInteger minPrice,
    BigInteger maxPrice,
    UUID vendorId,
    UUID hubId
) {}
