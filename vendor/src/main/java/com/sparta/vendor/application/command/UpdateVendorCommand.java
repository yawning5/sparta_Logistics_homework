package com.sparta.vendor.application.command;

import com.sparta.vendor.domain.vo.UserRole;
import com.sparta.vendor.domain.vo.VendorType;
import com.sparta.vendor.infrastructure.security.CustomUserDetails;
import com.sparta.vendor.presentation.dto.request.UpdateVendorRequestDTO;
import java.util.UUID;

public record UpdateVendorCommand(
    Long userId,
    UUID affiliationId,
    UserRole role,
    UUID vendorId,
    String vendorName,
    VendorType vendorType,
    String city,
    String street,
    String zipcode,
    UUID hubId
) {

    public static UpdateVendorCommand of(UUID id, CustomUserDetails user,
        UpdateVendorRequestDTO request) {
        return new UpdateVendorCommand(
            user.getUserId(),
            user.getAffiliationId(),
            user.getRole(),
            id,
            request.vendorName(), // null 허용
            request.vendorType() != null ? VendorType.valueOf(request.vendorType().name()) : null,
            request.city(),
            request.street(),
            request.zipcode(),
            request.hubId()
        );
    }
}
