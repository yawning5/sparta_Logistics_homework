package com.keepgoing.product.infrastructure.persistence.repository;

import com.keepgoing.product.application.repository.ProductRepository;
import com.keepgoing.product.domain.Product;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product findById(UUID productId) {
        return productJpaRepository.findById(productId)
            .orElseThrow(
                () -> new IllegalArgumentException("상품을 찾을 수 없습니다. ")
            );
    }

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }
}
