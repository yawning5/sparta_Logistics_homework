package com.keepgoing.product.infrastructure.config;

import com.keepgoing.product.domain.repository.ProductRepository;
import com.keepgoing.product.infrastructure.repository.JpaProductRepository;
import com.keepgoing.product.infrastructure.repository.ProductRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public ProductRepository productRepository(JpaProductRepository jpaProductRepository) {
        return new ProductRepositoryAdapter(jpaProductRepository);
    }
}
