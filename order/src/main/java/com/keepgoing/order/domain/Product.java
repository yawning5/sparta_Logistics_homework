package com.keepgoing.order.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public record Product (
    @Column(name = "product_id")
    UUID id,
    @Column(name = "product_name")
    String name
){
    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
