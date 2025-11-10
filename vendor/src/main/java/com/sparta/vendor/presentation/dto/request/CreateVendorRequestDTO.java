package com.keepgoing.vendor.presentation.dto.request;

import com.keepgoing.vendor.presentation.dto.VendorTypeDTO;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateVendorRequestDTO(
    @NotNull String vendorName,
    @NotNull VendorTypeDTO vendorType,
    @NotNull String city,
    @NotNull String street,
    @NotNull String zipcode,
    @NotNull UUID hubId
) {

}
