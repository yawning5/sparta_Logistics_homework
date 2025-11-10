package com.keepgoing.product.application.service;

import com.keepgoing.product.application.command.CreateProductCommand;
import com.keepgoing.product.application.command.DeleteProductCommand;
import com.keepgoing.product.application.command.GetProductCommand;
import com.keepgoing.product.application.command.UpdateProductCommand;
import com.keepgoing.product.application.dto.ProductResult;
import com.keepgoing.product.domain.entity.Product;
import com.keepgoing.product.domain.service.ProductDomainValidator;
import com.keepgoing.product.domain.vo.HubId;
import com.keepgoing.product.domain.vo.VendorId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductPersistenceService productPersistenceService;
    private final VendorClientService vendorClientService;

    // ---------------- 조회 ----------------
    public ProductResult getProduct(GetProductCommand command) {
        Product product = productPersistenceService.findById(command.productId());
        product.checkDeleted();
        ProductDomainValidator.validateGetPermission(command.role(), command.affiliationId(),
            product.getHubId().getId());
        return ProductResult.from(product);
    }

    // ---------------- 생성 ----------------
    public ProductResult createProduct(CreateProductCommand command) {
        ProductDomainValidator.validateCreatePermission(command.role(), command.affiliationId(),
            command.vendorId(), command.hubId());

        VendorId vendorId = VendorId.of(command.vendorId());
        vendorClientService.validationVendor(vendorId, command.token());

        HubId hubId = HubId.of(command.hubId());
        //TODO: hubValidationService.validationHub(vendorId, command.token());
        return productPersistenceService.saveCreateProduct(command, vendorId, hubId);
    }

    // ---------------- 수정 ----------------
    public ProductResult updateProduct(UpdateProductCommand command) {
        Product product = productPersistenceService.findById(command.productId());
        product.checkDeleted();

        ProductDomainValidator.validateUpdatePermission(
            command.role(),
            command.affiliationId(),
            product,
            command.vendorId(),
            command.hubId()
        );

        // ID 변경 검증
        validateIdChange(command.vendorId(), product.getVendorId().getId(),
            () -> vendorClientService.validationVendorId(command.vendorId(), command.token()));

        //TODO: hubValidationService.validationHub(vendorId, command.token());
//        validateIdChange(command.hubId(), product.getHubId().getId(),
//            () -> hubValidationService.validationHubId(command.hubId(), command.token()));

        return productPersistenceService.updateProduct(command);
    }

    private void validateIdChange(UUID newId, UUID currentId, Runnable validationCall) {
        if (newId != null && !newId.equals(currentId)) {
            validationCall.run();
        }
    }

    // ---------------- 삭제 ----------------
    public void deleteProduct(DeleteProductCommand command) {
        Product product = productPersistenceService.findById(command.productId());
        product.checkDeleted();

        ProductDomainValidator.validateDeletePermission(command.role(), command.affiliationId(),
            product.getHubId().getId());

        productPersistenceService.deleteProduct(command);
    }
}
