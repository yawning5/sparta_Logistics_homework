package com.keepgoing.vendor.presentation.controller;

import com.keepgoing.vendor.application.dto.VendorResult;
import com.keepgoing.vendor.application.service.VendorService;
import com.keepgoing.vendor.presentation.dto.BaseResponseDTO;
import com.keepgoing.vendor.presentation.dto.response.VendorResponseDTO;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/internal/vendors")
public class InternalVendorController {

    private final VendorService vendorService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'HUB', 'DELIVERY', 'COMPANY')")
    public ResponseEntity<?> getVendor(@PathVariable UUID id) {
        VendorResult vendorResult = vendorService.getVendor(id);
        VendorResponseDTO vendorResponseDTO = VendorResponseDTO.from(vendorResult);
        return ResponseEntity.ok(BaseResponseDTO.success(vendorResponseDTO));
    }
}
