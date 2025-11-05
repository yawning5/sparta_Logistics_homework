package com.keepgoing.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public record Store(
    UUID id,
    String name
){
    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
