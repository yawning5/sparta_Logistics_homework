package com.keepgoing.order.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationCancelInventoryRequest {
    @JsonProperty("productId")
    private UUID productId;

    @JsonProperty("hubId")
    private UUID hubId;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("action")
    private String action;

    private ReservationCancelInventoryRequest(UUID productId, UUID hubId, int quantity) {
        this.productId = productId;
        this.hubId = hubId;
        this.quantity = quantity;
        this.action = "CANCEL";
    }

    public static ReservationCancelInventoryRequest create(UUID productId, UUID hubId, int quantity) {
        return new ReservationCancelInventoryRequest(productId, hubId, quantity);
    }
}
