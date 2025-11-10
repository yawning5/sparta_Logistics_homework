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
    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("hub_id")
    private String hubId;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("idempotency_key")
    private String idempotencyKey;

    private ReservationInventoryRequest(String productId, String hubId, Integer quantity, String idempotencyKey) {
        this.productId = productId;
        this.hubId = hubId;
        this.quantity = quantity;
        this.idempotencyKey = idempotencyKey;
    }

    public static ReservationInventoryRequest create(String productId, String hubId, Integer quantity, String idempotencyKey) {
        return new ReservationInventoryRequest(productId, hubId, quantity, idempotencyKey);
    }
}
