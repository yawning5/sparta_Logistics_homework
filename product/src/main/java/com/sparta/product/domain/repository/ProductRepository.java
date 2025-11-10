package com.sparta.product.domain.repository;

import com.sparta.product.domain.entity.Product;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(UUID id);

    void delete(Product product);
}
