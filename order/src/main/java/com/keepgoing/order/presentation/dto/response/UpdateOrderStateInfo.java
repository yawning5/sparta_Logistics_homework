package com.keepgoing.order.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.keepgoing.order.domain.order.OrderState;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PROTECTED)
public record UpdateOrderStateInfo(

    @JsonProperty("order_id")
    UUID orderId,

    @JsonProperty("previous_state")
    OrderState previousState,

    @JsonProperty("current_state")
    OrderState currentState,

    @JsonProperty("updated_at")
    LocalDateTime updatedAt
) {

    @Override
    public UUID orderId() {
        return orderId;
    }

    @Override
    public OrderState previousState() {
        return previousState;
    }

    @Override
    public OrderState currentState() {
        return currentState;
    }

    @Override
    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    public static UpdateOrderStateInfo create(UUID orderId, OrderState previousState, OrderState currentState, LocalDateTime updatedAt ) {
        return UpdateOrderStateInfo.builder()
            .orderId(orderId)
            .previousState(previousState)
            .currentState(currentState)
            .updatedAt(updatedAt)
            .build();
    }
}
