package com.sparta.vendor.domain.entity;

import com.sparta.vendor.application.command.UpdateVendorCommand;
import com.sparta.vendor.application.exception.ErrorCode;
import com.sparta.vendor.application.exception.ForbiddenOperationException;
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

    private boolean isDeleted() {
        return getDeletedBy() != null && getDeletedAt() != null;
    }

    public void checkDeleted() {
        if (isDeleted()) {
            throw new ForbiddenOperationException(ErrorCode.VENDOR_DELETED);
        }
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

    public void updateVendor(UpdateVendorCommand command) {
        // vendorName 업데이트
        if (command.vendorName() != null) {
            this.vendorName = command.vendorName();
        }

        // vendorType 업데이트
        if (command.vendorType() != null) {
            this.vendorType = command.vendorType();
        }

        // address 업데이트 (city, street, zipcode)
        if (command.city() != null || command.street() != null || command.zipcode() != null) {
            this.address = Address.of(
                command.city() != null ? command.city() : this.address.getCity(),
                command.street() != null ? command.street() : this.address.getStreet(),
                command.zipcode() != null ? command.zipcode() : this.address.getZipCode()
            );
        }

        // hubId 업데이트 (MASTER만 사용, 서비스에서 권한 검증 후 호출)
        if (command.hubId() != null) {
            this.hubId = HubId.of(command.hubId());
        }
    }
}
