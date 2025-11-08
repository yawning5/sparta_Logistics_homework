package com.keepgoing.order.domain.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
public class Order{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // supplier
    @Column(name = "supplier_id", nullable = false)
    private UUID supplierId;

    @Column(name = "supplier_name", nullable = false)
    private String supplierName;

    // receiver
    @Column(name = "receiver_id", nullable = false)
    private UUID receiverId;

    @Column(name = "receiver_name", nullable = false)
    private String receiverName;

    // product
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "hub_id")
    private UUID hubId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_state", nullable = false)
    private OrderState orderState;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Column(name = "idempotency_key", nullable = false)
    private UUID idempotencyKey;

    @Column(name = "delivery_due_at", nullable = false)
    private LocalDateTime deliveryDueAt;

    @Column(name = "delivery_request_note")
    private String deliveryRequestNote;

    @Column(name = "order_at", nullable = false)
    private LocalDateTime orderedAt;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_by")
    private UUID deletedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Version
    @Column(name = "version")
    private Long version;

    @Builder
    private Order(
        UUID supplierId, String supplierName,
        UUID receiverId, String receiverName,
        UUID productId, String productName,
        UUID deliveryId, OrderState orderState,
        Integer quantity, Integer totalPrice, UUID idempotencyKey,
        LocalDateTime deliveryDueAt, String deliveryRequestNote,
        LocalDateTime orderedAt
    ) {
        if (supplierId == null) throw new IllegalArgumentException("supplierId 필수");
        if (receiverId == null) throw new IllegalArgumentException("receiverId 필수");
        if (productId == null) throw new IllegalArgumentException("productId 필수");
        if (quantity == null || quantity < 1) throw new IllegalArgumentException("quantity >= 1");
        if (totalPrice == null || totalPrice < 0) throw new IllegalArgumentException("totalPrice >= 0");
        if (orderedAt == null) throw new IllegalArgumentException("orderedAt 필수");
        if (deliveryDueAt == null || deliveryDueAt.isBefore(orderedAt))
            throw new IllegalArgumentException("deliveryDueAt은 orderedAt 이후여야 함");

        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.productId = productId;
        this.productName = productName;
        this.deliveryId = deliveryId;
        this.orderState = (orderState != null) ? orderState : OrderState.PENDING_VALIDATION;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.idempotencyKey = idempotencyKey;
        this.deliveryDueAt = deliveryDueAt;
        this.deliveryRequestNote = deliveryRequestNote;
        this.orderedAt = orderedAt;
    }

    public static Order create(
        UUID supplierId, String supplierName,
        UUID receiverId, String receiverName,
        UUID productId, String productName,
        Integer quantity, Integer totalPrice,
        LocalDateTime now,
        LocalDateTime deliveryDueAt, String deliveryRequestNote
    ) {
        return Order.builder()
            .supplierId(supplierId)
            .supplierName(supplierName)
            .receiverId(receiverId)
            .receiverName(receiverName)
            .productId(productId)
            .productName(productName)
            .orderState(OrderState.PENDING_VALIDATION)
            .quantity(quantity)
            .totalPrice(totalPrice)
            .idempotencyKey(UUID.randomUUID())
            .deliveryDueAt(deliveryDueAt)
            .deliveryRequestNote(deliveryRequestNote)
            .orderedAt(now)
            .build();
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) this.createdAt = now;
        if (this.updatedAt == null) this.updatedAt = now;
        if (this.orderState == null) this.orderState = OrderState.PENDING_VALIDATION;

    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void changeOrderStateToProductVerified() {
        orderState = OrderState.PRODUCT_VERIFIED;
    }

    public void changeOrderStateToFail() {
        orderState = OrderState.FAILED;
    }

    public void changeOrderStateToAwaitingPayment() {
        orderState = OrderState.AWAITING_PAYMENT;
    }

    public void changeOrderStateToPaid() {
        orderState = OrderState.PAID;
    }

    public void changeOrderStateToCompleted() {
        orderState = OrderState.COMPLETED;
    }

    public void changeOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public void registerHubId(UUID hubId) {
        this.hubId = hubId;
    }



}