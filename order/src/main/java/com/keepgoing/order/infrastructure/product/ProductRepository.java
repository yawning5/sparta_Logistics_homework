package com.keepgoing.order.infrastructure.product;

import com.keepgoing.order.domain.product.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {

}
