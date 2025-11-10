package com.keepgoing.vendor.domain.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.keepgoing.vendor.application.exception.ForbiddenOperationException;
import com.keepgoing.vendor.domain.vo.UserRole;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VendorDomainValidatorTest {

    @Test
    @DisplayName("HUB 권한으로 affiliationId와 hubId 불일치 시 ForbiddenOperationException 발생")
    void validateCreatePermissionHubForbidden() {
        // given
        UUID affiliationId = UUID.randomUUID();
        UUID hubId = UUID.randomUUID();

        // when & then
        assertThatThrownBy(() -> VendorDomainValidator.validateCreatePermission(
            UserRole.HUB, affiliationId, hubId))
            .isInstanceOf(ForbiddenOperationException.class)
            .hasMessageContaining("허브 관리자는 담당 허브에만 업체를 등록할 수 있습니다.");
    }

    @Test
    @DisplayName("HUB 권한으로 affiliationId와 hubId 일치 시 생성 허용")
    void validateCreatePermissionHubAllowed() {
        // given
        UUID affiliationId = UUID.randomUUID();
        UUID hubId = affiliationId; // 일치하도록 설정

        // when & then: 예외가 발생하지 않아야 함
        VendorDomainValidator.validateCreatePermission(UserRole.HUB, affiliationId, hubId);
    }

    @Test
    @DisplayName("MASTER 권한은 항상 생성 허용")
    void validateCreatePermissionMasterAllowed() {
        // given
        UUID affiliationId = UUID.randomUUID();
        UUID hubId = UUID.randomUUID();

        // when & then: 예외가 발생하지 않아야 함
        VendorDomainValidator.validateCreatePermission(UserRole.MASTER, affiliationId, hubId);
    }
}
