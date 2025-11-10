package com.sparta.vendor.application.command;

import com.sparta.vendor.domain.vo.UserRole;
import com.sparta.vendor.infrastructure.security.CustomUserDetails;
import java.util.UUID;
import org.springframework.security.core.userdetails.UserDetails;

public record DeleteVendorCommand(
    Long userId,
    UUID affiliationId,
    UserRole role,
    String token,
    UUID vendorId
) {

    public static DeleteVendorCommand of(UUID id, CustomUserDetails user) {
        return new DeleteVendorCommand(
            user.getUserId(),
            user.getAffiliationId(),
            user.getRole(),
            user.getToken(),
            id
        );
    }
}
