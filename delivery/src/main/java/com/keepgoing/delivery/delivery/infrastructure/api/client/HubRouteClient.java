package com.keepgoing.delivery.delivery.infrastructure.api.client;

import com.keepgoing.delivery.delivery.infrastructure.api.dto.HubRouteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(
        name = "hub-service"
)
public interface HubRouteClient {

    @GetMapping("/v1/hub-routes/{routeId}")
    HubRouteResponse getHubRoute(
            @PathVariable("routeId") UUID routeId,
            @RequestHeader("Authorization") String token
    );
}
