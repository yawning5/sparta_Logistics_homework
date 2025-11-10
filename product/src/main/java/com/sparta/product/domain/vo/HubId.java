package com.keepgoing.product.domain.vo;

import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class HubId {

    UUID id;

    public HubId(UUID id) {
        this.id = id;
    }

    public static HubId of(UUID id) {
        return new HubId(id);
    }
}
