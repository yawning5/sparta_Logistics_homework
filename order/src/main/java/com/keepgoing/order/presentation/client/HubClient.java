package com.keepgoing.order.presentation.client;

import com.keepgoing.order.presentation.dto.request.ReservationInventoryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "hub-service")
public interface HubClient {

    @PostMapping("/v1/inventory/allocate")
    Boolean reservationInventoryForProduct(@RequestBody ReservationInventoryRequest request);
}
