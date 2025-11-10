package com.keepgoing.vendor.application.service;

import com.keepgoing.vendor.application.command.CreateVendorCommand;
import com.keepgoing.vendor.application.command.DeleteVendorCommand;
import com.keepgoing.vendor.application.command.UpdateVendorCommand;
import com.keepgoing.vendor.application.dto.VendorResult;
import com.keepgoing.vendor.application.exception.ErrorCode;
import com.keepgoing.vendor.application.exception.VendorNotFoundException;
import com.keepgoing.vendor.domain.entity.Vendor;
import com.keepgoing.vendor.domain.repository.VendorRepository;
import com.keepgoing.vendor.domain.vo.Address;
import com.keepgoing.vendor.domain.vo.HubId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
}
