package com.keepgoing.vendor.infrastructure.repository;

import com.keepgoing.vendor.domain.entity.Vendor;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaVendorRepository extends JpaRepository<Vendor, UUID> {

}
