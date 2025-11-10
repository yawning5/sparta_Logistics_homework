package com.keepgoing.vendor.application.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.keepgoing.vendor.application.command.CreateVendorCommand;
import com.keepgoing.vendor.application.dto.VendorResult;
import com.keepgoing.vendor.application.exception.ForbiddenOperationException;
import com.keepgoing.vendor.domain.vo.UserRole;
import com.keepgoing.vendor.domain.vo.VendorType;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class VendorServiceTest {

    @Mock
    private VendorPersistenceService vendorPersistenceService;

    @InjectMocks
    private VendorService vendorService;

    private UUID hubId;

    @BeforeEach
    void setUp() {
        hubId = UUID.randomUUID();
    }

    @Test
    @DisplayName("MASTER 권한으로 벤더 생성 시 값 검증")
    void createVendorMasterValuesCheck() {
        // given
        CreateVendorCommand command = new CreateVendorCommand(
            1L,
            null, // affiliationId
            UserRole.MASTER,
            null,
            "테스트벤더",
            VendorType.RECEIVER,
            "서울시 강남구",
            "도승로 28 9",
            "12345",
            hubId
        );

        // 실제 반환값을 만들어 mock에 연결
        VendorResult expectedResult = new VendorResult(
            UUID.randomUUID(),
            command.vendorName(),
            command.vendorType(),
            command.city(),
            command.street(),
            command.zipcode(),
            command.hubId());

        when(vendorPersistenceService.saveCreateVendor(any(CreateVendorCommand.class)))
            .thenReturn(expectedResult);

        // when
        VendorResult result = vendorService.createVendor(command);

        // then
        verify(vendorPersistenceService, times(1)).saveCreateVendor(command);

        // assertThat으로 각 값이 command와 동일한지 확인
        assertThat(result.vendorName()).isEqualTo(command.vendorName());
        assertThat(result.vendorType()).isEqualTo(command.vendorType());
        assertThat(result.city()).isEqualTo(command.city());
        assertThat(result.street()).isEqualTo(command.street());
        assertThat(result.zipCode()).isEqualTo(command.zipcode());
        assertThat(result.hubId()).isEqualTo(command.hubId());

        // vendorId는 실제 생성된 값 확인
        assertThat(result.id()).isNotNull();
    }

    @Test
    @DisplayName("HUB 권한으로 벤더 생성 시 affiliationId와 hubId 불일치 -> 예외")
    void createVendorHubForbidden() {
        // given
        UUID affiliationId = UUID.randomUUID();
        UUID otherHubId = UUID.randomUUID(); // affiliationId와 다르게 설정
        CreateVendorCommand command = new CreateVendorCommand(
            2L,
            affiliationId,
            UserRole.HUB,
            null,
            "허브벤더",
            VendorType.RECEIVER,
            "서울시 강남구",
            "도승로 28 9",
            "12345",
            otherHubId
        );

        // when & then
        assertThatThrownBy(() -> vendorService.createVendor(command))
            .isInstanceOf(ForbiddenOperationException.class)
            .hasMessageContaining("허브 관리자는 담당 허브에만 업체를 등록할 수 있습니다.");

        // persistence 호출은 없어야 함
        verify(vendorPersistenceService, never()).saveCreateVendor(any());
    }

    @Test
    @DisplayName("HUB 권한으로 affiliationId와 hubId 일치 시 벤더 생성 성공 - 값 검증")
    void createVendorHubSuccess() {
        // given
        UUID affiliationId = hubId; // 일치하도록 설정
        CreateVendorCommand command = new CreateVendorCommand(
            3L,
            affiliationId,
            UserRole.HUB,
            null,
            "허브벤더",
            VendorType.RECEIVER,
            "서울시 강남구",
            "도승로 28 9",
            "12345",
            hubId
        );

        VendorResult expectedResult = new VendorResult(
            UUID.randomUUID(),
            command.vendorName(),
            command.vendorType(),
            command.city(),
            command.street(),
            command.zipcode(),
            command.hubId()
        );

        when(vendorPersistenceService.saveCreateVendor(any(CreateVendorCommand.class)))
            .thenReturn(expectedResult);

        // when
        VendorResult result = vendorService.createVendor(command);

        // then
        verify(vendorPersistenceService, times(1)).saveCreateVendor(command);

        // 모든 필드 검증
        assertThat(result.id()).isNotNull();
        assertThat(result.vendorName()).isEqualTo(command.vendorName());
        assertThat(result.vendorType()).isEqualTo(command.vendorType());
        assertThat(result.city()).isEqualTo(command.city());
        assertThat(result.street()).isEqualTo(command.street());
        assertThat(result.zipCode()).isEqualTo(command.zipcode());
        assertThat(result.hubId()).isEqualTo(command.hubId());

        // Role, affiliationId 검증도 필요하다면 추가
        assertThat(command.role()).isEqualTo(UserRole.HUB);
        assertThat(command.affiliationId()).isEqualTo(affiliationId);
    }

}
