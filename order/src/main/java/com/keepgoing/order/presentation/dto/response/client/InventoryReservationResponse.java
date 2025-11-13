package com.keepgoing.order.presentation.dto.response.client;

import java.util.UUID;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class InventoryReservationResponse {

    private UUID id;

    public boolean fail() {
        return id == null;
    }
}

