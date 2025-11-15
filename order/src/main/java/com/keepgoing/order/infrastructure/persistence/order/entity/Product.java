package com.keepgoing.order.infrastructure.persistence.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Column(name = "product_id", nullable = false)
    private UUID productId;
}
