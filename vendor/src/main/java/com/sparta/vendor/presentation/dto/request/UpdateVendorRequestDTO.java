package com.keepgoing.vendor.presentation.dto.request;

import com.keepgoing.vendor.presentation.dto.VendorTypeDTO;
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
