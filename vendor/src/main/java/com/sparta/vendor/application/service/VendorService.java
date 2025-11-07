package com.sparta.vendor.application.service;

import com.sparta.vendor.application.command.CreateVendorCommand;
import com.sparta.vendor.application.dto.VendorResult;
import com.sparta.vendor.application.exception.ErrorCode;
import com.sparta.vendor.application.exception.ForbiddenOperationException;
import com.sparta.vendor.application.exception.VendorDeletedException;
import com.sparta.vendor.application.exception.VendorNotFoundException;
import com.sparta.vendor.domain.entity.Vendor;
import com.sparta.vendor.domain.repository.VendorRepository;
import com.sparta.vendor.domain.vo.Address;
import com.sparta.vendor.domain.vo.HubId;
import com.sparta.vendor.domain.vo.UserRole;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;

    public VendorResult vendorCreate(CreateVendorCommand command) {

        UserRole role = command.role();

        if (role == UserRole.HUB && !command.affiliationId().equals(command.hubId())) {
            throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_HUB_OPERATION);
        }

        Address address = Address.of(command.city(), command.street(), command.zipcode());

        HubId hubId = HubId.of(command.hubId());

        Vendor vendor = Vendor.create(command.vendorName(), command.vendorType(), address, hubId);

        Vendor saveVendor = vendorRepository.save(vendor);

        return VendorResult.from(saveVendor);
    }

    public VendorResult getVendor(UUID id) {
        Vendor vendor = vendorRepository.findById(id)
            .orElseThrow(() -> new VendorNotFoundException(ErrorCode.VENDOR_NOT_FOUND));

        if (vendor.isDeleted()) {
            throw new VendorDeletedException(ErrorCode.VENDOR_DELETED);
        }

        return VendorResult.from(vendor);
    }
}
