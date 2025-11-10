package com.keepgoing.product.domain.repository;

import com.keepgoing.product.domain.entity.Product;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(UUID id);

    void delete(Product product);
}
