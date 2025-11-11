package com.sparta.hub.presentation;

import com.sparta.hub.application.command.CreateHubCommand;
import com.sparta.hub.application.command.UpdateHubCommand;
import com.sparta.hub.application.dto.HubResponse;
import com.sparta.hub.application.service.HubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/hubs")
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    @PostMapping
    public ResponseEntity<HubResponse> create(@RequestBody CreateHubCommand command) {
        return ResponseEntity.ok(hubService.createHub(command, 1L));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HubResponse> getHub(@PathVariable UUID id) {
        return ResponseEntity.ok(hubService.getHub(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HubResponse> update(@PathVariable UUID id, @RequestBody UpdateHubCommand command) {
        return ResponseEntity.ok(hubService.updateHub(id, command, 1L));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        hubService.deleteHub(id, 1L);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<HubResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String hubStatus,
            Pageable pageable) {
        return ResponseEntity.ok(hubService.searchHubs(name, address, hubStatus, pageable));
    }
}