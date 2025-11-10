package com.keepgoing.product.infrastructure.repository;

import com.keepgoing.product.domain.entity.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<Product, UUID> {

}
