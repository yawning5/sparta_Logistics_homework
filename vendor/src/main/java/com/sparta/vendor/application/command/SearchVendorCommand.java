package com.sparta.vendor.application.command;

import com.sparta.vendor.domain.vo.VendorType;
import com.sparta.vendor.presentation.dto.VendorTypeDTO;
import java.util.UUID;

public record SearchVendorCommand(
    UUID vendorId,
    String vendorName,
    VendorType vendorType,
    String address,
    String zipCode,
    UUID hubId
) {

    public static SearchVendorCommand of(UUID vendorId, String vendorName, VendorTypeDTO vendorType,
        String address, String zipCode, UUID hubId) {
        return new SearchVendorCommand(
            vendorId,
            vendorName,
            vendorType != null ? VendorType.valueOf(vendorType.name()) : null,
            address,
            zipCode,
            hubId);
    }
}
