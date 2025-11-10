package com.sparta.product.domain.service;

import com.sparta.product.application.exception.ErrorCode;
import com.sparta.product.application.exception.ForbiddenOperationException;
import com.sparta.product.domain.entity.Product;
import com.sparta.product.domain.vo.UserRole;
import lombok.experimental.UtilityClass;

import java.util.UUID;
import org.aspectj.bridge.ICommand;

@UtilityClass
public class ProductDomainValidator {

    // 조회 권한 검증
    public void validateGetPermission(UserRole role, UUID affiliationId, UUID hubId) {
        if (role == UserRole.HUB && !affiliationId.equals(hubId)) {
            throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_HUB_GET_OPERATION);
        }
    }

    // 생성 권한 검증
    public void validateCreatePermission(UserRole role, UUID affiliationId, UUID vendorId, UUID hubId) {
        switch(role) {
            case HUB -> {
                if (!affiliationId.equals(hubId)) throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_HUB_OPERATION);
            }
            case COMPANY -> {
                if (!affiliationId.equals(vendorId)) throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_COMPANY_OPERATION);
            }
        }
    }

    // 수정 권한 검증
    public void validateUpdatePermission(UserRole role, UUID affiliationId, Product product, UUID newVendorId, UUID newHubId) {
        if (role == UserRole.MASTER) return;

        switch (role) {
            case HUB -> {
                if (!affiliationId.equals(product.getHubId().getId())) {
                    throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_HUB_UPDATE_OPERATION);
                }
                if (newHubId != null) {
                    throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_HUB_ID_UPDATE_OPERATION);
                }
            }
            case COMPANY -> {
                if (!affiliationId.equals(product.getVendorId().getId())) {
                    throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_COMPANY_UPDATE_OPERATION);
                }
                if (newVendorId != null) {
                    throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_COMPANY_ID_UPDATE_OPERATION);
                }
            }
        }
    }

    public void validateDeletePermission(UserRole role, UUID affiliationId, UUID hubId) {
        if(role == UserRole.HUB && !affiliationId.equals(hubId)) {
            throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_DELETE_HUB);
        }
    }
}
