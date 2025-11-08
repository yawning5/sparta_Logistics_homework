package com.sparta.vendor.infrastructure.config;

import com.sparta.vendor.domain.repository.VendorRepository;
import com.sparta.vendor.infrastructure.repository.JpaVendorRepository;
import com.sparta.vendor.infrastructure.repository.VendorRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public VendorRepository vendorRepository(JpaVendorRepository jpaVendorRepository) {
        return new VendorRepositoryAdapter(jpaVendorRepository);
    }
}
