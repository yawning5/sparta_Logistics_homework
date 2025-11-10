package com.keepgoing.order.presentation.dto.response.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.keepgoing.order.domain.order.OrderState;
import java.util.UUID;

public record OrderStateInfo(
    @JsonProperty("order_id")
    UUID orderId,

    @JsonProperty("order_state")
    OrderState orderState
) {

    public static OrderStateInfo create(UUID orderId, OrderState orderState) {
        return new OrderStateInfo(orderId, orderState);
    }

}
