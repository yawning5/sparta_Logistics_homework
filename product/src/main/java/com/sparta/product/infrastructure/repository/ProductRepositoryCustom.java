package com.sparta.product.infrastructure.repository;

import com.sparta.product.application.command.SearchProductCommand;
import com.sparta.product.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<Product> searchProducts(SearchProductCommand command, Pageable pageable);
}
