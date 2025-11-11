package com.sparta.product.application.service;

import com.sparta.product.application.command.CreateProductCommand;
import com.sparta.product.application.command.DeleteProductCommand;
import com.sparta.product.application.command.SearchProductCommand;
import com.sparta.product.application.command.UpdateProductCommand;
import com.sparta.product.application.dto.ProductResult;
import com.sparta.product.application.exception.ErrorCode;
import com.sparta.product.application.exception.ProductDeletedException;
import com.sparta.product.application.exception.ProductNotFoundException;
import com.sparta.product.domain.entity.Product;
import com.sparta.product.domain.repository.ProductRepository;
import com.sparta.product.domain.vo.HubId;
import com.sparta.product.domain.vo.VendorId;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional
    public Page<Product> searchProducts(SearchProductCommand command, Pageable pageable) {
        return productRepository.searchVendors(command, pageable);
    }
}
