package com.keepgoing.order.presentation.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.keepgoing.order.domain.order.OrderState;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateOrderResponse {

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("order_state")
    private String orderState;

    @JsonProperty("ordered_at")
    private String orderedAt;

    @Builder
    private CreateOrderResponse(String orderState, String orderId, String orderedAt) {
        this.orderId = orderId;
        this.orderState = orderState;
        this.orderedAt = orderedAt;


    }

    public static CreateOrderResponse create(UUID orderId, OrderState orderState, LocalDateTime orderedAt) {
        return CreateOrderResponse.builder()
            .orderId(String.valueOf(orderId))
            .orderState(String.valueOf(orderState))
            .orderedAt(String.valueOf(orderedAt))
            .build();
    }
}

