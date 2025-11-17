package com.keepgoing.order.application.event.order;


import com.keepgoing.order.domain.order.Order;

import java.util.UUID;

public record OrderCreatedEvent(
        UUID orderId,
        UUID productId,
        Integer quantity,
        Integer totalPrice
) {
   public static OrderCreatedEvent from(Order order){
       return new OrderCreatedEvent(
               order.getId(),
               order.getProductId(),
               order.getQuantity(),
               order.getTotalPrice()
       );
   }
}
