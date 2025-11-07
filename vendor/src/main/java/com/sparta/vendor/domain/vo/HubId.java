package com.sparta.vendor.domain.vo;

import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class HubId {

    private UUID id;

    public HubId(UUID id) {
        this.id = id;
    }
}
