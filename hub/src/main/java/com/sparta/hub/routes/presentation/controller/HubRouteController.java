package com.sparta.hub.routes.presentation.controller;

import com.sparta.hub.routes.application.command.CreateHubRouteCommand;
import com.sparta.hub.routes.application.command.UpdateHubRouteCommand;
import com.sparta.hub.routes.application.dto.HubRouteResponse;
import com.sparta.hub.routes.application.query.HubRouteSearchQuery;
import com.sparta.hub.routes.application.service.HubRouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/hub-routes")
public class HubRouteController {

    private final HubRouteService hubRouteService;

    @PostMapping
    public ResponseEntity<HubRouteResponse> createRoute(@Valid @RequestBody CreateHubRouteCommand command) {
        HubRouteResponse response = hubRouteService.createRoute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HubRouteResponse> getRoute(@PathVariable UUID id) {
        HubRouteResponse response = hubRouteService.getRoute(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<HubRouteResponse>> searchRoutes(
            @RequestParam(required = false) UUID originHubId,
            @RequestParam(required = false) UUID destinationHubId,
            @RequestParam(required = false) Integer maxTransitMinutes,
            @RequestParam(required = false) BigDecimal maxDistanceKm,
            Pageable pageable
    ) {
        HubRouteSearchQuery query = new HubRouteSearchQuery(
                originHubId, destinationHubId, maxTransitMinutes, maxDistanceKm, pageable
        );
        Page<HubRouteResponse> response = hubRouteService.searchRoutes(query);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HubRouteResponse> updateRoute(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateHubRouteCommand command
    ) {
        HubRouteResponse response = hubRouteService.updateRoute(id, command);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable UUID id) {
        hubRouteService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }
}