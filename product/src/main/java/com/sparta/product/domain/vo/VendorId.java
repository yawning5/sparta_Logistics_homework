package com.keepgoing.product.domain.vo;

import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class VendorId {

    private UUID id;

    public VendorId(UUID id) {
        this.id = id;
    }

    public static VendorId of(UUID id) {
        return new VendorId(id);
    }
}
