package com.sparta.vendor.application.service;

import com.sparta.vendor.application.command.CreateVendorCommand;
import com.sparta.vendor.application.command.UpdateVendorCommand;
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

    private Vendor findById(UUID id) {
        return vendorRepository.findById(id)
            .orElseThrow(() -> new VendorNotFoundException(ErrorCode.VENDOR_NOT_FOUND));
    }

    private void checkDeleted(Vendor vendor) {
        if (vendor.isDeleted()) {
            throw new VendorDeletedException(ErrorCode.VENDOR_DELETED);
        }
    }

    public VendorResult vendorCreate(CreateVendorCommand command) {

        UserRole role = command.role();

        if (role == UserRole.HUB && !command.affiliationId().equals(command.hubId())) {
            throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_HUB_OPERATION);
        }

        //TODO: 존재하는 허브인지 확인하는 로직이 필요

        Address address = Address.of(command.city(), command.street(), command.zipcode());

        HubId hubId = HubId.of(command.hubId());

        Vendor vendor = Vendor.create(command.vendorName(), command.vendorType(), address, hubId);

        Vendor saveVendor = vendorRepository.save(vendor);

        return VendorResult.from(saveVendor);
    }

    public VendorResult getVendor(UUID id) {
        Vendor vendor = findById(id);
        checkDeleted(vendor);
        return VendorResult.from(vendor);
    }

    public VendorResult updateVendor(UpdateVendorCommand command,  UUID id) {
        Vendor vendor = findById(id);

        checkDeleted(vendor);

        if (command.role() == UserRole.MASTER) {
            validateMasterHubId(command, vendor);
        } else {
            validateRole(command, vendor);
        }
        vendor.updateVendor(command);
        Vendor updateVendor = vendorRepository.save(vendor);
        return VendorResult.from(updateVendor);
    }

    private void validateRole(UpdateVendorCommand command, Vendor vendor) {
        switch (command.role()) {
            case HUB -> {
                if (!command.affiliationId().equals(vendor.getHubId().getId())) {
                    throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_HUB_UPDATE_OPERATION);
                }
                if (command.hubId() != null) {
                    throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_HUB_ID_MODIFICATION);
                }
            }
            case COMPANY -> {
                if (!command.affiliationId().equals(vendor.getId())) {
                    throw new ForbiddenOperationException(
                        ErrorCode.FORBIDDEN_COMPONY_UPDATE_OPERATION);
                }
                if (command.hubId() != null) {
                    throw new ForbiddenOperationException(
                        ErrorCode.FORBIDDEN_COMPONY_ID_MODIFICATION);
                }
            }
        }
    }

    private void validateMasterHubId(UpdateVendorCommand command, Vendor vendor) {
        if (command.hubId() != null && !command.hubId().equals(vendor.getHubId().getId())) {
            //TODO: hub 검증하는 client 필요함
        }
    }
}
