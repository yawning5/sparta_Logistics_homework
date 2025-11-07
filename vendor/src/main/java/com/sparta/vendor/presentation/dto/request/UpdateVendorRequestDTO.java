package com.sparta.vendor.presentation.dto.request;

import com.sparta.vendor.presentation.dto.VendorTypeDTO;
import java.util.UUID;

public record UpdateVendorRequestDTO(
    String vendorName,
    VendorTypeDTO vendorType,
    String city,
    String street,
    String zipcode,
    UUID hubId
) {

}
