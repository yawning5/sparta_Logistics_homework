package com.keepgoing.order.application.event.stock;

import com.keepgoing.order.application.event.payment.Payment;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {
    @Id
    private UUID id;

    private String name;
    private Integer stock; // 재고

    public static Stock create(UUID productId) {
        Stock stock = new Stock();
        stock.id = productId;
        stock.name = "노트북";
        stock.stock = 10;
        return stock;
    }

    public void decreaseStock(Integer quantity) {
        if (this.stock < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다");
        }
        this.stock -= quantity;
    }
}
