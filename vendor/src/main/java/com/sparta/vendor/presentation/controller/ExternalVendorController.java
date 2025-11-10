package com.keepgoing.vendor.presentation.controller;

import com.keepgoing.vendor.application.command.CreateVendorCommand;
import com.keepgoing.vendor.application.command.DeleteVendorCommand;
import com.keepgoing.vendor.application.command.UpdateVendorCommand;
import com.keepgoing.vendor.application.dto.VendorResult;
import com.keepgoing.vendor.application.service.VendorService;
import com.keepgoing.vendor.infrastructure.security.CustomUserDetails;
import com.keepgoing.vendor.presentation.dto.BaseResponseDTO;
import com.keepgoing.vendor.presentation.dto.request.CreateVendorRequestDTO;
import com.keepgoing.vendor.presentation.dto.request.UpdateVendorRequestDTO;
import com.keepgoing.vendor.presentation.dto.response.VendorResponseDTO;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/vendors")
public class ExternalVendorController {

    private final VendorService vendorService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "OK";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER', 'HUB')")
    public ResponseEntity<?> createVendor(@AuthenticationPrincipal CustomUserDetails user,
        @RequestBody CreateVendorRequestDTO request) {

        CreateVendorCommand command = CreateVendorCommand.of(user, request);

        VendorResult vendorResult = vendorService.createVendor(command);

        VendorResponseDTO vendorResponseDTO = VendorResponseDTO.from(vendorResult);

        return ResponseEntity.ok(BaseResponseDTO.success(vendorResponseDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'HUB', 'DELIVERY', 'COMPANY')")
    public ResponseEntity<?> getVendor(@PathVariable UUID id) {
        VendorResult vendorResult = vendorService.getVendor(id);
        VendorResponseDTO vendorResponseDTO = VendorResponseDTO.from(vendorResult);
        return ResponseEntity.ok(BaseResponseDTO.success(vendorResponseDTO));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'HUB', 'COMPANY')")
    public ResponseEntity<?> updateVendor(@PathVariable UUID id,
        @AuthenticationPrincipal CustomUserDetails user,
        @RequestBody UpdateVendorRequestDTO request) {

        UpdateVendorCommand command = UpdateVendorCommand.of(id, user, request);

        VendorResult vendorResult = vendorService.updateVendor(command);
        VendorResponseDTO vendorResponseDTO = VendorResponseDTO.from(vendorResult);
        return ResponseEntity.ok(BaseResponseDTO.success(vendorResponseDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'HUB')")
    public ResponseEntity<?> deleteVendor(@PathVariable UUID id,
        @AuthenticationPrincipal CustomUserDetails user
    ) {
        DeleteVendorCommand command = DeleteVendorCommand.of(id, user);

        vendorService.deleteVendor(command);
        return ResponseEntity.ok(BaseResponseDTO.ok());
    }
}
