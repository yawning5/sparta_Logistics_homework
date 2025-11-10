package com.keepgoing.vendor.domain.vo;

import lombok.Getter;

@Getter
public enum VendorType {

    PRODUCER("생산업체"),
    RECEIVER("수령업체");

    private final String description;

    VendorType(String description) {
        this.description = description;
    }
}
