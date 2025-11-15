package com.keepgoing.order.infrastructure.persistence.mapper;

import com.keepgoing.order.domain.model.Order;
import com.keepgoing.order.infrastructure.persistence.order.entity.OrderEntity;

public final class OrderMapper {

    private OrderMapper() {

    }

    public static OrderEntity toEntityForCreate(Order order) {
        return OrderEntity.create(
            order.getMember().memberId(),
            order.getSupplier().vendorId(),
            order.getReceiver().vendorId(),
            order.getProduct().productId(),
            order.getDelivery().deliveryDueAt(),
            order.getDelivery().deliveryRequestNote(),
            order.getQuantity(),
            order.getTotalPrice(),
            order.getOrderState(),
            order.getCancelState(),
            order.getIdempotencyKey(),
            order.getOrderedAt()
        );
    }

    public static Order toDomain(OrderEntity orderEntity) {
        return Order.of(
            orderEntity.getId(),
            orderEntity.getMember().getMemberId(),
            orderEntity.getSupplier().getVendorId(),
            orderEntity.getReceiver().getVendorId(),
            orderEntity.getProduct().getProductId(),
            orderEntity.getHub() == null ? null : orderEntity.getHub().getHubId(),
            orderEntity.getDelivery() == null ? null : orderEntity.getDelivery().getDeliveryId(),
            orderEntity.getDelivery().getDeliveryDueAt(),
            orderEntity.getDelivery().getDeliveryRequestNote(),
            orderEntity.getQuantity(),
            orderEntity.getTotalPrice(),
            orderEntity.getOrderState(),
            orderEntity.getCancelState(),
            orderEntity.getIdempotencyKey(),
            orderEntity.getOrderedAt(),
            orderEntity.getConfirmedAt(),
            orderEntity.getCancelledAt());
    }
}
