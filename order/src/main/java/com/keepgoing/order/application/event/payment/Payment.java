package com.keepgoing.order.application.event.payment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @Id
    private UUID id;

    private UUID orderId;

    private UUID productId;

    private String status; // PENDING, COMPLETED, FAILED

    private Integer quantity;

    private Integer totalPrice;

    public static Payment create(UUID orderId, UUID productId, Integer quantity, Integer totalPrice) {
        Payment payment = new Payment();
        payment.id = UUID.randomUUID();
        payment.orderId = orderId;
        payment.productId = productId;
        payment.status = "PENDING";
        payment.quantity = quantity;
        payment.totalPrice = totalPrice;
        return payment;
    }

    public void complete() {
        this.status = "COMPLETED";
    }

    public void fail() {
        this.status = "FAILED";
    }
}
