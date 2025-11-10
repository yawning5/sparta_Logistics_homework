package com.keepgoing.order.presentation.dto.response.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.keepgoing.order.domain.order.Order;
import com.keepgoing.order.domain.order.OrderState;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PROTECTED)
public record OrderInfo(

    @JsonProperty("order_id")
    UUID orderId,

    @JsonProperty("member_id")
    Long memberId,

    @JsonProperty("supplier_id")
    UUID supplierId,

    @JsonProperty("supplier_name")
    String supplierName,

    @JsonProperty("receiver_id")
    UUID receiverId,

    @JsonProperty("receiver_name")
    String receiverName,

    @JsonProperty("product_id")
    UUID productId,

    @JsonProperty("product_name")
    String productName,

    @JsonProperty("delivery_id")
    UUID deliveryId,

    @JsonProperty("quantity")
    Integer quantity,

    @JsonProperty("total_price")
    Integer totalPrice,

    @JsonProperty("order_state")
    OrderState state,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("ordered_at")
    LocalDateTime orderedAt,

    @JsonProperty("confirmed_at")
    LocalDateTime confirmedAt,

    @JsonProperty("cancelled_at")
    LocalDateTime cancelledAt,

    @JsonProperty("delivery_due_at")
    LocalDateTime deliveryDueAt,

    @JsonProperty("delivery_request_note")
    String deliveryRequestNote,

    @JsonProperty("created_at")
    LocalDateTime createdAt,

    @JsonProperty("updated_at")
    LocalDateTime updatedAt

) {

    @Override
    public UUID orderId() {
        return orderId;
    }

    @Override
    public Long memberId() {
        return memberId;
    }

    @Override
    public UUID supplierId() {
        return supplierId;
    }

    @Override
    public String supplierName() {
        return supplierName;
    }

    @Override
    public UUID receiverId() {
        return receiverId;
    }

    @Override
    public String receiverName() {
        return receiverName;
    }

    @Override
    public UUID productId() {
        return productId;
    }

    @Override
    public String productName() {
        return productName;
    }

    @Override
    public UUID deliveryId() {
        return deliveryId;
    }

    @Override
    public Integer quantity() {
        return quantity;
    }

    @Override
    public Integer totalPrice() {
        return totalPrice;
    }

    @Override
    public OrderState state() {
        return state;
    }

    @Override
    public LocalDateTime orderedAt() {
        return orderedAt;
    }

    @Override
    public LocalDateTime confirmedAt() {
        return confirmedAt;
    }

    @Override
    public LocalDateTime cancelledAt() {
        return cancelledAt;
    }

    @Override
    public LocalDateTime deliveryDueAt() {
        return deliveryDueAt;
    }

    @Override
    public String deliveryRequestNote() {
        return deliveryRequestNote;
    }

    @Override
    public LocalDateTime createdAt() {
        return createdAt;
    }

    @Override
    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    @Builder
    private static OrderInfo create(
        Long memberId,
        UUID orderId, UUID supplierId, String supplierName, UUID receiverId,
        String receiverName, UUID productId, String productName, UUID deliveryId, Integer quantity,
        Integer totalPrice, OrderState state, LocalDateTime orderedAt, LocalDateTime confirmedAt,
        LocalDateTime cancelledAt, LocalDateTime deliveryDueAt, String deliveryRequestNote,
        LocalDateTime createdAt, LocalDateTime updatedAt) {

        return OrderInfo.builder()
            .orderId(orderId)
            .memberId(memberId)
            .supplierId(supplierId)
            .supplierName(supplierName)
            .receiverId(receiverId)
            .receiverName(receiverName)
            .productId(productId)
            .productName(productName)
            .deliveryId(deliveryId)
            .quantity(quantity)
            .totalPrice(totalPrice)
            .state(state)
            .orderedAt(orderedAt)
            .confirmedAt(confirmedAt)
            .cancelledAt(cancelledAt)
            .deliveryDueAt(deliveryDueAt)
            .deliveryRequestNote(deliveryRequestNote)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
    }

    public static OrderInfo from(Order order) {
        return OrderInfo.builder()
            .orderId(order.getId())
            .memberId(order.getMemberId())
            .supplierId(order.getSupplierId())
            .supplierName(order.getSupplierName())
            .receiverId(order.getReceiverId())
            .receiverName(order.getReceiverName())
            .productId(order.getProductId())
            .productName(order.getProductName())
            .deliveryId(order.getDeliveryId())
            .quantity(order.getQuantity())
            .totalPrice(order.getTotalPrice())
            .state(order.getOrderState())
            .orderedAt(order.getOrderedAt())
            .confirmedAt(order.getConfirmedAt())
            .cancelledAt(order.getCancelledAt())
            .deliveryDueAt(order.getDeliveryDueAt())
            .deliveryRequestNote(order.getDeliveryRequestNote())
            .createdAt(order.getCreatedAt())
            .updatedAt(order.getUpdatedAt())
            .build();
    }

}
