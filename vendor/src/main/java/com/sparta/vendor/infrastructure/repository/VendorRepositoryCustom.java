package com.sparta.vendor.infrastructure.repository;

import com.sparta.vendor.application.command.SearchVendorCommand;
import com.sparta.vendor.domain.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VendorRepositoryCustom {

    Page<Vendor> searchVendors(SearchVendorCommand command, Pageable pageable);
}
