package com.sparta.vendor.domain.entity;

import com.sparta.vendor.domain.vo.Address;
import com.sparta.vendor.domain.vo.HubId;
import com.sparta.vendor.domain.vo.VendorType;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Vendor {

    private UUID id;

    private String vendorName;

    private VendorType vendorType;

    private Address address;

    private HubId hubId;

    public Vendor(String vendorName, VendorType vendorType, Address address, HubId hubId) {
        this.id = UUID.randomUUID();
        this.vendorName = vendorName;
        this.vendorType = vendorType;
        this.address = address;
        this.hubId = hubId;
    }
}
