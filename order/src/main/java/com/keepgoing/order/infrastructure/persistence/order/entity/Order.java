package com.keepgoing.order.infrastructure.persistence.order.entity;

import com.keepgoing.order.domain.state.CancelState;
import com.keepgoing.order.domain.state.OrderState;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;

@Getter
@Entity
@Table(name = "p_order")
@FilterDef(
    name = "softDeleteFilter",
    defaultCondition = "deleted_at IS NULL",
    autoEnabled = true, // 항상 기본적으로 필터를 켜는 옵션
    applyToLoadByKey = true // PK 기반 단건 조회에도 필더 적용
)
@Filter(name = "softDeleteFilter")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    private Member member;

    @Embedded
    @AttributeOverrides(
        @AttributeOverride(name = "vendorId", column = @Column(name = "supplier_id"))
    )
    private Vendor supplier;

    @Embedded
    @AttributeOverrides(
        @AttributeOverride(name = "vendorId", column = @Column(name = "receiver_id"))
    )
    private Vendor receiver;

    @Embedded
    private Product product;

    @Embedded
    private Hub hub;

    @Embedded
    private Delivery delivery;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_state", nullable = false)
    private OrderState orderState;

    @Enumerated(EnumType.STRING)
    @Column(name = "cancel_state", nullable = false)
    private CancelState cancelState;

    @Column(name = "idempotency_key", nullable = false)
    private UUID idempotencyKey;

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
    private Long deletedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Version
    @Column(name = "version")
    private Long version;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) this.createdAt = now;
        if (this.updatedAt == null) this.updatedAt = now;
        if (this.orderState == null) this.orderState = OrderState.PENDING_VALIDATION;
        if (this.cancelState == null) this.cancelState = CancelState.NONE;

    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
//
//if (memberId == null) throw new IllegalArgumentException("memberId 필수");
//        if (supplierId == null) throw new IllegalArgumentException("supplierId 필수");
//        if (receiverId == null) throw new IllegalArgumentException("receiverId 필수");
//        if (productId == null) throw new IllegalArgumentException("productId 필수");
//        if (quantity == null || quantity < 1) throw new IllegalArgumentException("quantity >= 1");
//        if (totalPrice == null || totalPrice < 0) throw new IllegalArgumentException("totalPrice >= 0");
//        if (orderedAt == null) throw new IllegalArgumentException("orderedAt 필수");
//        if (deliveryDueAt == null || deliveryDueAt.isBefore(orderedAt))
//    throw new IllegalArgumentException("deliveryDueAt은 orderedAt 이후여야 함");