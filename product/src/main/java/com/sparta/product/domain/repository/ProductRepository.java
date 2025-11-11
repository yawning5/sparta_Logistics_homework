package com.sparta.product.domain.repository;

import com.sparta.product.application.command.SearchProductCommand;
import com.sparta.product.domain.entity.Product;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(UUID id);

    void delete(Product product);

    Page<Product> searchVendors(SearchProductCommand command, Pageable pageable);
}
