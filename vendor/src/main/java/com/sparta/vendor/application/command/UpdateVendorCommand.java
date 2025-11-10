package com.keepgoing.vendor.application.command;

import com.keepgoing.vendor.domain.vo.UserRole;
import com.keepgoing.vendor.domain.vo.VendorType;
import com.keepgoing.vendor.infrastructure.security.CustomUserDetails;
import com.keepgoing.vendor.presentation.dto.request.UpdateVendorRequestDTO;
import java.util.UUID;

public record UpdateVendorCommand(
    Long userId,
    UUID affiliationId,
    UserRole role,
    String token,
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
            user.getToken(),
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
