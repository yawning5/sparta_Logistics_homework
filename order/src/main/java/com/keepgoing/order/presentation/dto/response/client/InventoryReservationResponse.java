package com.keepgoing.order.presentation.dto.response.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class InventoryReservationResponse {

    @JsonProperty("success")
    private boolean success;

    public boolean fail() {
        return !success;
    }
}

