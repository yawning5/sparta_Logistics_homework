package com.keepgoing.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public record DeliveryRequestNote (
    @Column(name = "delivery_due_at")
    LocalDateTime deliveryDueAt,
    @Column(name = "delivery_request_note")
    String deliveryRequestNote
){
    public boolean inValidDeliveryDueAt(LocalDateTime now) {
        if (deliveryDueAt == null || deliveryDueAt.isBefore(now)) return true;
        return false;
    }
}
