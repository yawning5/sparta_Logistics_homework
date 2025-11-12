package com.sparta.product.application.service;

import com.sparta.product.application.command.CreateProductCommand;
import com.sparta.product.application.command.DeleteProductCommand;
import com.sparta.product.application.command.GetProductCommand;
import com.sparta.product.application.command.SearchProductCommand;
import com.sparta.product.application.command.UpdateProductCommand;
import com.sparta.product.application.dto.ProductResult;
import com.sparta.product.application.exception.ErrorCode;
import com.sparta.product.application.exception.ForbiddenOperationException;
import com.sparta.product.domain.entity.Product;
import com.sparta.product.domain.service.ProductDomainValidator;
import com.sparta.product.domain.vo.HubId;
import com.sparta.product.domain.vo.UserRole;
import com.sparta.product.domain.vo.VendorId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductPersistenceService productPersistenceService;
    private final VendorClientService vendorClientService;
    private final HubClientService hubClientService;

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
        hubClientService.validationHub(hubId, command.token());
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

        validateIdChange(command.hubId(), product.getHubId().getId(),
            () -> hubClientService.validationHubId(command.hubId(), command.token()));

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

    public Page<ProductResult> searchProducts(SearchProductCommand command, Pageable pageable) {

        if (command.role() == UserRole.HUB) {
            UUID userHubId = command.affiliationId();
            UUID requestedHubId = command.hubId();

            if (requestedHubId != null && !requestedHubId.equals(userHubId)) {
                throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_HUB_GET_OPERATION);
            }

            // 새로운 Command 생성
            command = SearchProductCommand.of(
                command.userId(),
                command.affiliationId(),
                command.role(),
                command.productId(),
                command.name(),
                command.description(),
                command.minPrice(),
                command.maxPrice(),
                command.vendorId(),
                userHubId // hubId를 HUB의 affiliationId로 설정
            );
        }

        Page<Product> products = productPersistenceService.searchProducts(command, pageable);
        return products.map(ProductResult::from);
    }
}
