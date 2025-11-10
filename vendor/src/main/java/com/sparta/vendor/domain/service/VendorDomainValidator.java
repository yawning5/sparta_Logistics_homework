package com.keepgoing.vendor.domain.service;

import com.keepgoing.vendor.application.exception.ErrorCode;
import com.keepgoing.vendor.application.exception.ForbiddenOperationException;
import com.keepgoing.vendor.domain.entity.Vendor;
import com.keepgoing.vendor.domain.vo.UserRole;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VendorDomainValidator {

    public void validateCreatePermission(UserRole role, UUID affiliationId, UUID hubId) {
        if (role == UserRole.HUB && !affiliationId.equals(hubId)) {
            throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_HUB_OPERATION);
        }
    }

    /**
     * 수정 권한 검증
     */
    public void validateUpdatePermission(UserRole role, UUID affiliationId, Vendor vendor,
        UUID newHubId) {
        if (role == UserRole.MASTER) {
            return; // MASTER는 모든 권한 허용
        }

        switch (role) {
            case HUB -> validateHubUpdate(affiliationId, vendor, newHubId);
            case COMPANY -> validateCompanyUpdate(affiliationId, vendor, newHubId);
        }
    }

    private void validateHubUpdate(UUID affiliationId, Vendor vendor, UUID newHubId) {
        if (!affiliationId.equals(vendor.getHubId().getId())) {
            throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_HUB_UPDATE_OPERATION);
        }
        if (newHubId != null) {
            throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_HUB_ID_MODIFICATION);
        }
    }

    private void validateCompanyUpdate(UUID affiliationId, Vendor vendor, UUID newHubId) {
        if (!affiliationId.equals(vendor.getId())) {
            throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_COMPANY_UPDATE_OPERATION);
        }
        if (newHubId != null) {
            throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_COMPANY_ID_MODIFICATION);
        }
    }

    public void validateDeletePermission(UserRole role, UUID affiliationId, UUID hubId) {
        if (role == UserRole.HUB && !affiliationId.equals(hubId)) {
            throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_DELETE_HUB);
        }
    }
}
