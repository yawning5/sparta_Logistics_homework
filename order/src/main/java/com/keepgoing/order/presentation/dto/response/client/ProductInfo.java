package com.keepgoing.order.presentation.dto.response.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductInfo {

    @JsonProperty("id")
    private String productId;

    @JsonProperty("productName")
    private String productName;

    @JsonProperty("hubId")
    private String hubId;

    @JsonProperty("productPrice")
    private String productPrice;

}
