package com.sparta.product.infrastructure.external.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VendorResponseDTO {

    private UUID id;
    private String vendorName;
    private String vendorType;
    private String city;
    private String street;
    private String zipCode;
    private UUID hubId;
}
