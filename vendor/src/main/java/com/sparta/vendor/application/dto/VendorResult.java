package com.sparta.vendor.application.dto;

import com.sparta.vendor.domain.entity.Vendor;
import com.sparta.vendor.domain.vo.VendorType;
import java.util.UUID;
import lombok.Builder;


public record VendorResult(
    UUID id,
    String vendorName,
    VendorType vendorType,
    String city,
    String street,
    String zipCode,
    UUID hubId
) {

    public static VendorResult from(Vendor vendor) {
        return new VendorResult(
            vendor.getId(),
            vendor.getVendorName(),
            vendor.getVendorType(),
            vendor.getAddress().getCity(),
            vendor.getAddress().getStreet(),
            vendor.getAddress().getZipCode(),
            vendor.getHubId().getId()
        );
    }

    @Builder
    public static VendorResult of(
        UUID id,
        String vendorName,
        VendorType vendorType,
        String city,
        String street,
        String zipCode,
        UUID hubId
    ) {
        return new VendorResult(id, vendorName, vendorType, city, street, zipCode, hubId);
    }
}
