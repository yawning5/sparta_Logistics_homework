package com.keepgoing.order.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "supplier_id")),
        @AttributeOverride(name = "name", column = @Column(name = "supplier_name"))
    })
    private Store supplier;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "receiver_id")),
        @AttributeOverride(name = "name", column = @Column(name = "receiver_name"))
    })
    private Store receiver;

    @Embedded
    private Product product;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_state")
    private OrderState orderState;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Embedded
    private DeliveryRequestNote deliveryRequestNote;

    @Column(name = "order_at")
    private LocalDateTime orderedAt;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedBy;

    @Column(name = "deleted_by")
    private UUID deletedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Version
    @Column(name = "version")
    private Long version;

    @Builder
    private Order(Store supplier, Store receiver, Product product, UUID deliveryId, OrderState orderState,
        Integer quantity, Integer totalPrice, DeliveryRequestNote deliveryRequestNote,
        LocalDateTime orderedAt, LocalDateTime createdAt) {
        this.supplier = supplier;
        this.receiver = receiver;
        this.product = product;
        this.deliveryId = deliveryId;
        this.orderState = orderState;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.deliveryRequestNote = deliveryRequestNote;
        this.orderedAt = orderedAt;
        this.createdAt = createdAt;
    }

    public static Order create(Store supplier, Store receiver, Product product,
        Integer quantity, Integer totalPrice, LocalDateTime now, DeliveryRequestNote deliveryRequestNote ) {

        if (supplier == null) throw new IllegalArgumentException("공급 업체는 필수입니다.");
        if (receiver == null) throw new IllegalArgumentException("수령 업체는 필수입니다.");
        if (product == null) throw new IllegalArgumentException("주문 상품은 필수입니다.");
        if (quantity < 1) throw new IllegalArgumentException("주문 수량은 1개 이상이어야 합니다.");
        if (totalPrice < 0) throw new IllegalArgumentException("주문 금액은 음수가 될 수 없습니다.");
        if (deliveryRequestNote.inValidDeliveryDueAt(now))
            throw new IllegalArgumentException("납품 기간은 필수이며 주문 시간보다 이전일 수 없습니다.");

        return Order.builder()
            .supplier(supplier)
            .receiver(receiver)
            .product(product)
            .orderState(OrderState.PENDING_VALIDATION)
            .quantity(quantity)
            .totalPrice(totalPrice)
            .deliveryRequestNote(deliveryRequestNote)
            .orderedAt(now)
            .createdAt(now)
            .build();
    }

}
