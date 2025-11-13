package com.keepgoing.order.presentation.dto.response.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductInfo {

    @JsonProperty("id")
    private UUID productId;

    @JsonProperty("productName")
    private String productName;

    @JsonProperty("productDescription")
    private String productDescription;

    @JsonProperty("productPrice")
    private BigInteger productPrice;

    @JsonProperty("vendorId")
    private UUID vendorId;

    @JsonProperty("hubId")
    private UUID hubId;

}