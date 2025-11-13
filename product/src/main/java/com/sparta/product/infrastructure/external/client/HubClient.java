package com.sparta.product.infrastructure.external.client;

import com.sparta.product.infrastructure.external.dto.HubResponseDTO;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "hub-service")
public interface HubClient {

    @GetMapping("/v1/hubs/{id}")
    HubResponseDTO getHubById(@RequestHeader("Authorization") String token,
        @PathVariable UUID id);

}
