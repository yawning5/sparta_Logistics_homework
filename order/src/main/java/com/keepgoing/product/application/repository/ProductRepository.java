package com.keepgoing.product.application.repository;

import com.keepgoing.product.domain.Product;
import java.util.UUID;

public interface ProductRepository {

    Product findById(UUID productId);

    Product save(Product product);

}
