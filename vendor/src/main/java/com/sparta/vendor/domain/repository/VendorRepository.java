package com.sparta.vendor.domain.repository;

import com.sparta.vendor.application.command.SearchVendorCommand;
import com.sparta.vendor.domain.entity.Vendor;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VendorRepository {

    Vendor save(Vendor vendor);

    Optional<Vendor> findById(UUID id);

    void delete(Vendor vendor);

    // 검색 + 페이징 + 정렬 기능
    Page<Vendor> searchVendors(SearchVendorCommand command, Pageable pageable);
}
