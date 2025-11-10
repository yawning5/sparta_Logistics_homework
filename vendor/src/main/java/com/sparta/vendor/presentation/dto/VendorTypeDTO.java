package com.sparta.vendor.presentation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VendorTypeDTO {
    PRODUCER("생산업체"),
    RECEIVER("수령업체");

    private final String description;
}
