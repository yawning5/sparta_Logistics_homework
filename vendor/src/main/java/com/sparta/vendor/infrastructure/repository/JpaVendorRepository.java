package com.sparta.vendor.infrastructure.repository;

import com.sparta.vendor.domain.entity.Vendor;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaVendorRepository extends JpaRepository<Vendor, UUID> {

}
