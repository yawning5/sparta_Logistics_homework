package com.sparta.vendor.presentation.controller;

import com.sparta.vendor.application.command.CreateVendorCommand;
import com.sparta.vendor.application.command.UpdateVendorCommand;
import com.sparta.vendor.application.dto.VendorResult;
import com.sparta.vendor.application.service.VendorService;
import com.sparta.vendor.infrastructure.security.CustomUserDetails;
import com.sparta.vendor.presentation.dto.BaseResponseDTO;
import com.sparta.vendor.presentation.dto.request.CreateVendorRequestDTO;
import com.sparta.vendor.presentation.dto.request.UpdateVendorRequestDTO;
import com.sparta.vendor.presentation.dto.response.VendorResponseDTO;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @GetMapping("/v1/vendors/health-check")
    public String healthCheck() {
        return "OK";
    }

    @PostMapping("/v1/vendors")
    @PreAuthorize("hasAnyRole('MASTER', 'HUB')")
    public ResponseEntity<?> createVendor(@AuthenticationPrincipal CustomUserDetails user,
        @RequestBody CreateVendorRequestDTO request) {

        CreateVendorCommand command = CreateVendorCommand.of(user, request);

        VendorResult vendorResult = vendorService.vendorCreate(command);

        VendorResponseDTO vendorResponseDTO = VendorResponseDTO.from(vendorResult);

        return ResponseEntity.ok(BaseResponseDTO.success(vendorResponseDTO));
    }

    @GetMapping("/v1/vendors/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'HUB', 'DELIVERY', 'COMPANY')")
    public ResponseEntity<?> getVendor(@PathVariable UUID id) {
        VendorResult vendorResult = vendorService.getVendor(id);
        VendorResponseDTO vendorResponseDTO = VendorResponseDTO.from(vendorResult);
        return ResponseEntity.ok(BaseResponseDTO.success(vendorResponseDTO));
    }

    @PatchMapping("/v1/vendors/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'HUB', 'COMPANY')")
    public ResponseEntity<?> updateVendor(@PathVariable UUID id,
        @AuthenticationPrincipal CustomUserDetails user,
        @RequestBody UpdateVendorRequestDTO request) {

        UpdateVendorCommand command = UpdateVendorCommand.of(user, request);

        VendorResult vendorResult = vendorService.updateVendor(command, id);
        VendorResponseDTO vendorResponseDTO = VendorResponseDTO.from(vendorResult);
        return ResponseEntity.ok(BaseResponseDTO.success(vendorResponseDTO));
    }
}
