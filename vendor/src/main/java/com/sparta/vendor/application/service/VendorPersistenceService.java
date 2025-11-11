package com.sparta.vendor.application.service;

import com.sparta.vendor.application.command.CreateVendorCommand;
import com.sparta.vendor.application.command.DeleteVendorCommand;
import com.sparta.vendor.application.command.SearchVendorCommand;
import com.sparta.vendor.application.command.UpdateVendorCommand;
import com.sparta.vendor.application.dto.VendorResult;
import com.sparta.vendor.application.exception.ErrorCode;
import com.sparta.vendor.application.exception.VendorNotFoundException;
import com.sparta.vendor.domain.entity.Vendor;
import com.sparta.vendor.domain.repository.VendorRepository;
import com.sparta.vendor.domain.vo.Address;
import com.sparta.vendor.domain.vo.HubId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VendorPersistenceService {

    private final VendorRepository vendorRepository;

    @Transactional(readOnly = true)
    public Vendor findById(UUID id) {
        return vendorRepository.findById(id)
            .orElseThrow(() -> new VendorNotFoundException(ErrorCode.VENDOR_NOT_FOUND));
    }

    @Transactional
    public VendorResult saveCreateVendor(CreateVendorCommand command) {
        Address address = Address.of(command.city(), command.street(), command.zipcode());
        HubId hubId = HubId.of(command.hubId());

        Vendor vendor = Vendor.create(command.vendorName(), command.vendorType(), address, hubId);

        Vendor savedVendor = vendorRepository.save(vendor);
        return VendorResult.from(savedVendor);
    }

    @Transactional
    public VendorResult updateVendor(UpdateVendorCommand command) {
        Vendor vendor = findById(command.vendorId());
        vendor.updateVendor(command);
        return VendorResult.from(vendor);
    }

    @Transactional
    public void deleteVendor(DeleteVendorCommand command) {
        Vendor vendor = findById(command.vendorId());
        vendor.delete(command.userId());
    }

    @Transactional(readOnly = true)
    public Page<Vendor> searchVendors(SearchVendorCommand command, Pageable pageable) {
        return vendorRepository.searchVendors(command, pageable);
    }
}
