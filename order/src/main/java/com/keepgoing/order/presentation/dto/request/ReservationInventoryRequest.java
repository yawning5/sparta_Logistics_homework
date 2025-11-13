package com.keepgoing.order.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationInventoryRequest {
    @JsonProperty("productId")
    private String productId;

    @JsonProperty("hubId")
    private String hubId;

    @JsonProperty("reservedQuantity")
    private Integer quantity;

    private ReservationInventoryRequest(String productId, String hubId, Integer quantity) {
        this.productId = productId;
        this.hubId = hubId;
        this.quantity = quantity;
    }

    public static ReservationInventoryRequest create(String productId, String hubId, Integer quantity) {
        return new ReservationInventoryRequest(productId, hubId, quantity);
    }
}
