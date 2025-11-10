package com.keepgoing.product.domain.entity;

import com.keepgoing.product.application.command.UpdateProductCommand;
import com.keepgoing.product.application.exception.ErrorCode;
import com.keepgoing.product.application.exception.ForbiddenOperationException;
import com.keepgoing.product.domain.vo.VendorId;
import com.keepgoing.product.domain.vo.HubId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigInteger;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_product")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String productName;

    private String productDescription;

    private BigInteger productPrice;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "vendor_id", nullable = false))
    })
    private VendorId vendorId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "hub_id", nullable = false))
    })
    private HubId hubId;

    public Product(String productName, String productDescription, BigInteger productPrice,
        VendorId vendorId, HubId hubId) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.vendorId = vendorId;
        this.hubId = hubId;
    }

    public static Product create(String productName, String productDescription,
        BigInteger productPrice,
        VendorId vendorId, HubId hubId) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name은 필수 입니다.");
        }

        if (productDescription == null || productDescription.isBlank()) {
            throw new IllegalArgumentException("Product description은 필수 입니다.");
        }

        if (productPrice == null) {
            throw new IllegalArgumentException("Product price는 필수 입니다.");
        }

        if (vendorId == null) {
            throw new IllegalArgumentException("Vendor Id는 필수 입니다.");
        }

        if (hubId == null) {
            throw new IllegalArgumentException("HubId는 필수 입니다.");
        }
        return new Product(productName, productDescription, productPrice, vendorId, hubId);
    }

    private boolean isDeleted() {
        return getDeletedBy() != null && getDeletedAt() != null;
    }

    public void checkDeleted() {
        if (isDeleted()) {
            throw new ForbiddenOperationException(ErrorCode.PRODUCT_DELETED);
        }
    }

    public void updateProduct(UpdateProductCommand command) {
        //productName 업데이트
        if (command.productName() != null) {
            this.productName = command.productName();
        }

        //productDescription 업데이트
        if (command.productDescription() != null) {
            this.productDescription = command.productDescription();
        }

        //productPrice 업데이트
        if (command.productPrice() != null) {
            this.productPrice = command.productPrice();
        }

        //vendorId 업데이트
        if (command.vendorId() != null) {
            this.vendorId = VendorId.of(command.vendorId());
        }

        //hubId 업데이트
        if (command.hubId() != null) {
            this.hubId = HubId.of(command.hubId());
        }
    }
}
