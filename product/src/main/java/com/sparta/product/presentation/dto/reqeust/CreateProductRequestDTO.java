package com.keepgoing.product.presentation.dto.reqeust;

import jakarta.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.UUID;

public record CreateProductRequestDTO(
    @NotNull String productName,
    @NotNull String productDescription,
    @NotNull BigInteger productPrice,
    @NotNull UUID vendorId,
    @NotNull UUID hubId
) {

}
