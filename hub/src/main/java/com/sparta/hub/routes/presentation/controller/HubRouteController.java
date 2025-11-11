package com.sparta.hub.routes.presentation.controller;

import com.sparta.hub.routes.application.command.CreateHubRouteCommand;
import com.sparta.hub.routes.application.dto.HubRouteResponse;
import com.sparta.hub.routes.application.service.HubRouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}