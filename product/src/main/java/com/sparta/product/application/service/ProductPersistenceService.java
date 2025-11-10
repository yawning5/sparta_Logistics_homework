package com.keepgoing.product.application.service;

import com.keepgoing.product.application.command.CreateProductCommand;
import com.keepgoing.product.application.command.DeleteProductCommand;
import com.keepgoing.product.application.command.UpdateProductCommand;
import com.keepgoing.product.application.dto.ProductResult;
import com.keepgoing.product.application.exception.ErrorCode;
import com.keepgoing.product.application.exception.ProductDeletedException;
import com.keepgoing.product.application.exception.ProductNotFoundException;
import com.keepgoing.product.domain.entity.Product;
import com.keepgoing.product.domain.repository.ProductRepository;
import com.keepgoing.product.domain.vo.HubId;
import com.keepgoing.product.domain.vo.VendorId;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductPersistenceService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Product findById(UUID id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Transactional
    public ProductResult saveCreateProduct(CreateProductCommand command, VendorId vendorId,
        HubId hubId) {
        Product product = Product.create(
            command.productName(),
            command.productDescription(),
            command.productPrice(),
            vendorId,
            hubId
        );
        productRepository.save(product);
        return ProductResult.from(product);
    }

    @Transactional
    public ProductResult updateProduct(UpdateProductCommand command) {
        Product product = findById(command.productId());
        product.updateProduct(command);
        return ProductResult.from(product);
    }

    @Transactional
    public void deleteProduct(DeleteProductCommand command) {
        Product product = findById(command.productId());
        product.delete(command.userId());
    }
}
