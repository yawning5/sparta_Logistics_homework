package com.sparta.vendor.application.service;

import com.sparta.vendor.application.command.CreateVendorCommand;
import com.sparta.vendor.application.command.DeleteVendorCommand;
import com.sparta.vendor.application.command.SearchVendorCommand;
import com.sparta.vendor.application.command.UpdateVendorCommand;
import com.sparta.vendor.application.dto.VendorResult;
import com.sparta.vendor.domain.entity.Vendor;
import com.sparta.vendor.domain.service.VendorDomainValidator;
import com.sparta.vendor.domain.vo.HubId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorPersistenceService vendorPersistenceService;
    private final HubClientService hubClientService;

    // --------------------- 조회 ---------------------
    public VendorResult getVendor(UUID id) {
        Vendor vendor = vendorPersistenceService.findById(id);
        vendor.checkDeleted();
        return VendorResult.from(vendor);
    }

    // --------------------- 생성 ---------------------
    public VendorResult createVendor(CreateVendorCommand command) {

        VendorDomainValidator.validateCreatePermission(command.role(), command.affiliationId(),
            command.hubId());

        HubId hubId = HubId.of(command.hubId());
        hubClientService.validationHub(hubId, command.token());

        return vendorPersistenceService.saveCreateVendor(command);
    }

    // --------------------- 수정 ---------------------
    public VendorResult updateVendor(UpdateVendorCommand command) {
        Vendor vendor = vendorPersistenceService.findById(command.vendorId());
        vendor.checkDeleted();

        VendorDomainValidator.validateUpdatePermission(command.role(), command.affiliationId(),
            vendor, command.hubId());

        validateIdChange(command.hubId(), vendor.getHubId().getId(),
            () -> hubClientService.validationHubId(command.hubId(), command.token()));

        return vendorPersistenceService.updateVendor(command);
    }

    private void validateIdChange(UUID newId, UUID currentId, Runnable validationCall) {
        if (newId != null && !newId.equals(currentId)) {
            validationCall.run();
        }
    }

    // --------------------- 삭제 ---------------------
    public void deleteVendor(DeleteVendorCommand command) {
        Vendor vendor = vendorPersistenceService.findById(command.vendorId());
        vendor.checkDeleted();
        VendorDomainValidator.validateDeletePermission(command.role(), command.affiliationId(),
            vendor.getHubId().getId());
        vendorPersistenceService.deleteVendor(command);
    }

    // --------------------- 검색 ---------------------
    public Page<VendorResult> searchVendors(SearchVendorCommand command, Pageable pageable) {
        Page<Vendor> vendors = vendorPersistenceService.searchVendors(command, pageable);
        return vendors.map(VendorResult::from);
    }
}
