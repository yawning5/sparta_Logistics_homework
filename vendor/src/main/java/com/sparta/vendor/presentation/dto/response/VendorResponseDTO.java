package com.keepgoing.vendor.presentation.dto.response;

import com.keepgoing.vendor.application.dto.VendorResult;
import com.keepgoing.vendor.presentation.dto.VendorTypeDTO;
import java.util.UUID;

public record VendorResponseDTO(
    UUID id,
    String vendorName,
    VendorTypeDTO vendorType,
    String city,
    String street,
    String zipCode,
    UUID hubId
) {

    public static VendorResponseDTO from(VendorResult vendorResult) {
        return new VendorResponseDTO(
            vendorResult.id(),
            vendorResult.vendorName(),
            VendorTypeDTO.valueOf(vendorResult.vendorType().name()),
            vendorResult.city(),
            vendorResult.street(),
            vendorResult.zipCode(),
            vendorResult.hubId()
        );
    }
}
