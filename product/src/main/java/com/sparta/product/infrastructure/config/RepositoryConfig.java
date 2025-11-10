package com.sparta.product.infrastructure.config;

import com.sparta.product.domain.repository.ProductRepository;
import com.sparta.product.infrastructure.repository.JpaProductRepository;
import com.sparta.product.infrastructure.repository.ProductRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public ProductRepository productRepository(JpaProductRepository jpaProductRepository) {
        return new ProductRepositoryAdapter(jpaProductRepository);
    }
}
