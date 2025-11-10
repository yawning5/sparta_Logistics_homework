package com.keepgoing.vendor.infrastructure.config;

import com.keepgoing.vendor.domain.repository.VendorRepository;
import com.keepgoing.vendor.infrastructure.repository.JpaVendorRepository;
import com.keepgoing.vendor.infrastructure.repository.VendorRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public VendorRepository vendorRepository(JpaVendorRepository jpaVendorRepository) {
        return new VendorRepositoryAdapter(jpaVendorRepository);
    }
}
