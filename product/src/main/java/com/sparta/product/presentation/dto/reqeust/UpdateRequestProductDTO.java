package com.keepgoing.product.presentation.dto.reqeust;

import java.math.BigInteger;
import java.util.UUID;

public record UpdateRequestProductDTO(
    String productName,
    String productDescription,
    BigInteger productPrice,
    UUID vendorId,
    UUID hubId
) {

}
