package com.keepgoing.delivery.deliveryperson.domain.entity;

public record DeliverySeq(int value) {
    public DeliverySeq {
        if (value < 1) {
            throw new IllegalArgumentException("DeliverySeq must be positive");
        }
    }
}
