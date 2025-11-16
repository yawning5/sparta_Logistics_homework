package com.keepgoing.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;

@Entity
@Getter
public class Product {
    @Id
    private UUID id;

    private String name;
    private int stock; // 재고

    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다");
        }
        this.stock -= quantity;
    }
}