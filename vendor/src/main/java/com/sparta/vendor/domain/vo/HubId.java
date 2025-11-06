package com.sparta.vendor.domain.vo;

import java.util.UUID;
import lombok.Getter;

@Getter
public class HubId {

    private UUID id;

    public HubId(UUID id) {
        this.id = id;
    }
}
