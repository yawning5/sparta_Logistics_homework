package com.keepgoing.order.presentation.dto.response.client;

import java.util.UUID;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class InventoryReservationResponse {

    private UUID id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String hubStatus;
    private String createAt;
    private String updateAt;
    private String deleteAt;

    public boolean fail() {
        return id == null;
    }
}

