package com.keepgoing.vendor.application.command;

import com.keepgoing.vendor.domain.vo.UserRole;
import com.keepgoing.vendor.domain.vo.VendorType;
import com.keepgoing.vendor.infrastructure.security.CustomUserDetails;
import com.keepgoing.vendor.presentation.dto.request.CreateVendorRequestDTO;
import java.util.UUID;

public record CreateVendorCommand(
    Long userId,
    UUID affiliationId,
    UserRole role,
    String token,
    String vendorName,
    VendorType vendorType,
    String city,
    String street,
    String zipcode,
    UUID hubId

) {

    public static CreateVendorCommand of(CustomUserDetails user,
        CreateVendorRequestDTO createVendorRequestDTO) {
        return new CreateVendorCommand(
            user.getUserId(),
            user.getAffiliationId(),
            user.getRole(),
            user.getToken(),
            createVendorRequestDTO.vendorName(),
            VendorType.valueOf(createVendorRequestDTO.vendorType().name()),
            createVendorRequestDTO.city(),
            createVendorRequestDTO.street(),
            createVendorRequestDTO.zipcode(),
            createVendorRequestDTO.hubId()
        );
    }
}
