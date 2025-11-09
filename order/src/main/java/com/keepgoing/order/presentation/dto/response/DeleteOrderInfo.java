package com.keepgoing.order.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

public record DeleteOrderInfo (

    @JsonProperty("order_id")
    UUID orderId,

    @JsonProperty("deleted_by")
    Long memberId,

    @JsonProperty("deleted_at")
    LocalDateTime deletedAt
) {

    public static DeleteOrderInfo create(UUID orderId, Long memberId, LocalDateTime deletedAt) {
        return new DeleteOrderInfo(orderId, memberId, deletedAt);
    }
}
