package com.sparta.vendor.infrastructure.repository;

import com.sparta.vendor.domain.entity.Vendor;
import com.sparta.vendor.domain.repository.VendorRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VendorRepositoryAdapter implements VendorRepository {

    private final JpaVendorRepository jpaVendorRepository;

    @Override
    public Vendor save(Vendor vendor) {
        return jpaVendorRepository.save(vendor);
    }

    @Override
    public Optional<Vendor> findById(UUID id) {
        return jpaVendorRepository.findById(id);
    }

    @Override
    public void delete(Vendor vendor) {
        jpaVendorRepository.delete(vendor);
    }
}
