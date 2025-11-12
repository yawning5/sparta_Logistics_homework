package com.sparta.vendor.presentation.dto.request;

import com.sparta.vendor.presentation.dto.VendorTypeDTO;
import java.util.UUID;

public record SearchVendorRequestDTO(
    UUID vendorId,
    String vendorName,
    VendorTypeDTO vendorType,
    String address,
    String zipCode,
    UUID hubId
) {

}
