package com.keepgoing.delivery.deliveryperson.domain.entity;

public record DeliverySeq(int value) {

    public DeliverySeq {
        if (value < 1) {
            throw new IllegalArgumentException("DeliverySeq must be between 1 and 10");
        }
    }

    public DeliverySeq next() {
        return new DeliverySeq(this.value + 1);
    }
}
