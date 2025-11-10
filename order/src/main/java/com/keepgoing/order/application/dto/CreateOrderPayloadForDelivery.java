package com.keepgoing.order.application.dto;


import com.keepgoing.order.domain.order.OrderState;
import java.util.UUID;

public record CreateOrderPayloadForDelivery(
    UUID orderId,
    UUID receiverId,
    UUID productId,
    OrderState state
) {

}
