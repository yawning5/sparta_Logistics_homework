package com.sparta.hub.inventory.domain.entity;

import com.sparta.hub.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "p_hub_inventory")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HubInventory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID hubId;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String status;

    public static HubInventory create(UUID hubId, UUID productId, int quantity) {
        return HubInventory.builder()
                .hubId(hubId)
                .productId(productId)
                .quantity(quantity)
                .status("AVAILABLE")
                .build();
    }

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }
}
