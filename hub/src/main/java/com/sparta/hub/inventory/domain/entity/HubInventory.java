package com.sparta.hub.inventory.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "p_inventory")
@Getter
@NoArgsConstructor
public class HubInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // 허브가 관리하는 상품 식별자

    @Column(name = "product_id", nullable = false)
    private UUID productId; // 상품 식별자

    @Column(name = "product_name", nullable = false)
    private String productName; // 상품 이름

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity; // 재고 수량

    @Column(name = "product_status", nullable = false)
    private String productStatus; // 상품 상태 (AVAILABLE, RESERVED, SHIPPED, ADJUSTED 등)

    @Column(name = "hub_id", nullable = false)
    private UUID hubId; // 허브 식별자

    @Column(name = "hub_name", nullable = false)
    private String hubName; // 허브 이름

    @Version
    private Long version; // 낙관적 락을 위한 버전 컬럼

    private HubInventory(UUID hubId, String hubName, UUID productId, String productName, int stockQuantity) {
        this.hubId = hubId;
        this.hubName = hubName;
        this.productId = productId;
        this.productName = productName;
        this.stockQuantity = stockQuantity;
        this.productStatus = "AVAILABLE";
    }

    public static HubInventory create(UUID hubId, String hubName, UUID productId, String productName, int stockQuantity) {
        return new HubInventory(hubId, hubName, productId, productName, stockQuantity);
    }

    public void increaseQuantity(int value) {
        if (value < 0) throw new IllegalArgumentException("수량은 음수가 될 수 없습니다.");
        this.stockQuantity += value;
    }

    public void decreaseQuantity(int value) {
        if (value > this.stockQuantity) throw new IllegalArgumentException("재고가 부족합니다.");
        this.stockQuantity -= value;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
        this.stockQuantity = quantity;
    }

    public void setProductStatus(String status) {
        if (status == null || status.isBlank()) throw new IllegalArgumentException("상태는 필수입니다.");
        this.productStatus = status;
    }

    public String getStatus() {
        return this.productStatus;
    }

    public int getQuantity() {
        return this.stockQuantity;
    }
}