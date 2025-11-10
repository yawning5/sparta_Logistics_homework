package com.keepgoing.vendor.domain.repository;

import com.keepgoing.vendor.domain.entity.Vendor;
import java.util.Optional;
import java.util.UUID;

public interface VendorRepository {

    Vendor save(Vendor vendor);

    Optional<Vendor> findById(UUID id);

    void delete(Vendor vendor);
}
