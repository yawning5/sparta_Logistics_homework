package com.keepgoing.order.infrastructure.persistence.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Column(name = "delivery_due_at", nullable = false)
    private LocalDateTime deliveryDueAt;

    @Column(name = "delivery_request_note")
    private String deliveryRequestNote;
}
