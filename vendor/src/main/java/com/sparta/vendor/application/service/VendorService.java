package com.sparta.vendor.application.service;

import com.sparta.vendor.application.command.CreateVendorCommand;
import com.sparta.vendor.application.command.DeleteVendorCommand;
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

    // --------------------- 조회 ---------------------
    private Vendor findById(UUID id) {
        return vendorRepository.findById(id)
            .orElseThrow(() -> new VendorNotFoundException(ErrorCode.VENDOR_NOT_FOUND));
    }

    private void checkDeleted(Vendor vendor) {
        if (vendor.isDeleted()) {
            throw new VendorDeletedException(ErrorCode.VENDOR_DELETED);
        }
    }

    public VendorResult getVendor(UUID id) {
        Vendor vendor = findById(id);
        checkDeleted(vendor);
        return VendorResult.from(vendor);
    }

    // --------------------- 생성 ---------------------
    public VendorResult vendorCreate(CreateVendorCommand command) {

        validateHubCreatePermission(command);

        // TODO: 존재하는 허브인지 확인하는 로직 필요

        Address address = Address.of(command.city(), command.street(), command.zipcode());
        HubId hubId = HubId.of(command.hubId());

        Vendor vendor = Vendor.create(command.vendorName(), command.vendorType(), address, hubId);

        Vendor savedVendor = vendorRepository.save(vendor);
        return VendorResult.from(savedVendor);
    }

    private void validateHubCreatePermission(CreateVendorCommand command) {
        if (command.role() == UserRole.HUB && !command.affiliationId().equals(command.hubId())) {
            throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_HUB_OPERATION);
        }
    }

    // --------------------- 수정 ---------------------
    public VendorResult updateVendor(UpdateVendorCommand command) {
        Vendor vendor = findById(command.vendorId());
        checkDeleted(vendor);

        if (command.role() == UserRole.MASTER) {
            validateMasterHubId(command, vendor);
        } else {
            validateUpdateRole(command, vendor);
        }

        vendor.updateVendor(command);
        Vendor updatedVendor = vendorRepository.save(vendor);
        return VendorResult.from(updatedVendor);
    }

    private void validateUpdateRole(UpdateVendorCommand command, Vendor vendor) {
        switch (command.role()) {
            case HUB -> validateHubUpdate(command, vendor);
            case COMPANY -> validateCompanyUpdate(command, vendor);
        }
    }

    private void validateHubUpdate(UpdateVendorCommand command, Vendor vendor) {
        if (!command.affiliationId().equals(vendor.getHubId().getId())) {
            throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_HUB_UPDATE_OPERATION);
        }
        if (command.hubId() != null) {
            throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_HUB_ID_MODIFICATION);
        }
    }

    private void validateCompanyUpdate(UpdateVendorCommand command, Vendor vendor) {
        if (!command.affiliationId().equals(vendor.getId())) {
            throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_COMPONY_UPDATE_OPERATION);
        }
        if (command.hubId() != null) {
            throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_COMPONY_ID_MODIFICATION);
        }
    }

    private void validateMasterHubId(UpdateVendorCommand command, Vendor vendor) {
        if (command.hubId() != null && !command.hubId().equals(vendor.getHubId().getId())) {
            // TODO: hub 검증하는 client 필요
        }
    }

    // --------------------- 삭제 ---------------------
    public void deleteVendor(DeleteVendorCommand command) {
        Vendor vendor = findById(command.vendorId());

        checkHubDeletePermission(command, vendor);

        vendor.deleteVendor(command.userId());

        vendorRepository.save(vendor);
    }

    private void checkHubDeletePermission(DeleteVendorCommand command, Vendor vendor) {
        if (command.role() == UserRole.HUB && !command.affiliationId()
            .equals(vendor.getHubId().getId())) {
            throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_DELETE_HUB);
        }
    }
}
