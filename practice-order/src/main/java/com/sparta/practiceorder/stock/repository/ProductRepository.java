package com.sparta.practiceorder.stock.repository;

import com.sparta.practiceorder.stock.domain.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {

}
