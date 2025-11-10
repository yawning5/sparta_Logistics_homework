package com.sparta.vendor.application.command;

import com.sparta.vendor.domain.vo.UserRole;
import com.sparta.vendor.domain.vo.VendorType;
import com.sparta.vendor.infrastructure.security.CustomUserDetails;
import com.sparta.vendor.presentation.dto.request.CreateVendorRequestDTO;
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
