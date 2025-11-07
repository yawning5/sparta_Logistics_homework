package com.sparta.vendor.domain.entity;

import com.sparta.vendor.domain.vo.Address;
import com.sparta.vendor.domain.vo.HubId;
import com.sparta.vendor.domain.vo.VendorType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_vendor")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Vendor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String vendorName;

    @Enumerated(EnumType.STRING)
    private VendorType vendorType;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "city", column = @Column(name = "city")),
        @AttributeOverride(name = "street", column = @Column(name = "street")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "zipcode"))
    })
    private Address address;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "hub_id", nullable = false))
    })
    private HubId hubId;

    public boolean isDeleted() {
        return getDeletedBy() != null && getDeletedAt() != null;
    }

    private Vendor(String vendorName, VendorType vendorType, Address address, HubId hubId) {
        this.vendorName = vendorName;
        this.vendorType = vendorType;
        this.address = address;
        this.hubId = hubId;
    }

    public static Vendor create(String vendorName, VendorType vendorType, Address address,
        HubId hubId) {
        if (vendorName == null || vendorName.isBlank()) {
            throw new IllegalArgumentException("vendorName은 필수입니다.");
        }
        if (vendorType == null) {
            throw new IllegalArgumentException("vendorType은 필수입니다.");
        }
        if (address == null) {
            throw new IllegalArgumentException("address는 필수입니다.");
        }
        if (hubId == null) {
            throw new IllegalArgumentException("hubId는 필수입니다.");
        }

        return new Vendor(vendorName, vendorType, address, hubId);
    }
}
