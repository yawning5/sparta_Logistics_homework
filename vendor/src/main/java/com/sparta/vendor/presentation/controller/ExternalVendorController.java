package com.sparta.vendor.presentation.controller;

import com.sparta.vendor.application.command.CreateVendorCommand;
import com.sparta.vendor.application.command.DeleteVendorCommand;
import com.sparta.vendor.application.command.SearchVendorCommand;
import com.sparta.vendor.application.command.UpdateVendorCommand;
import com.sparta.vendor.application.dto.VendorResult;
import com.sparta.vendor.application.service.VendorService;
import com.sparta.vendor.infrastructure.security.CustomUserDetails;
import com.sparta.vendor.presentation.dto.BaseResponseDTO;
import com.sparta.vendor.presentation.dto.PageResponseDTO;
import com.sparta.vendor.presentation.dto.VendorTypeDTO;
import com.sparta.vendor.presentation.dto.request.CreateVendorRequestDTO;
import com.sparta.vendor.presentation.dto.request.SearchVendorRequestDTO;
import com.sparta.vendor.presentation.dto.request.UpdateVendorRequestDTO;
import com.sparta.vendor.presentation.dto.response.VendorResponseDTO;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        @Valid @RequestBody CreateVendorRequestDTO request) {

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
        @Valid @RequestBody UpdateVendorRequestDTO request) {

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

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('MASTER', 'HUB', 'DELIVERY', 'COMPANY')")
    public ResponseEntity<?> searchVendors(
        Pageable pageable,
        @ModelAttribute SearchVendorRequestDTO request
    ) {

        int size = pageable.getPageSize();
        if (size != 10 && size != 30 && size != 50) {
            size = 10;
        }

        pageable = PageRequest.of(
            pageable.getPageNumber(),
            size,
            pageable.getSort()
        );

        SearchVendorCommand command = SearchVendorCommand.of(
            request.vendorId(),
            request.vendorName(),
            request.vendorType(),
            request.address(),
            request.zipCode(),
            request.hubId());

        Page<VendorResult> vendorResultPage = vendorService.searchVendors(command, pageable);

        Page<VendorResponseDTO> responsePage = vendorResultPage.map(VendorResponseDTO::from);

        PageResponseDTO<VendorResponseDTO> pageResponseDTO = PageResponseDTO.from(responsePage);

        return ResponseEntity.ok(BaseResponseDTO.success(pageResponseDTO));
    }
}
