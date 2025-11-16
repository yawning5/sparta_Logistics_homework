package com.keepgoing.product.infrastructure.persistence.repository;

import com.keepgoing.product.domain.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, UUID> {

}
